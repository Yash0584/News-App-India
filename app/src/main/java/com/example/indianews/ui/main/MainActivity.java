package com.example.indianews.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.indianews.R;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.databinding.ActivityMainBinding;
import com.example.indianews.ui.adapter.NewsAdapter;
import com.example.indianews.ui.adapter.TrendingNewsAdapter;
import com.example.indianews.ui.detail.DetailActivity;
import com.example.indianews.ui.favorites.FavoritesActivity;
import com.example.indianews.utils.Constants;
import com.example.indianews.utils.NetworkUtils;
import com.example.indianews.viewmodel.NewsViewModel;
import com.google.android.material.chip.Chip;

public class MainActivity extends AppCompatActivity {
    
    private ActivityMainBinding binding;
    private NewsViewModel viewModel;
    private NewsAdapter adapter;
    private TrendingNewsAdapter trendingAdapter;
    
    private String currentCountry = Constants.COUNTRY_INDIA;
    private String currentCategory = Constants.CATEGORY_TOP;
    private String currentLanguage = Constants.LANGUAGE_ENGLISH;
    private boolean isSearchMode = false;
    
    private Handler searchHandler;
    private Runnable searchRunnable;
    
    // Auto-scroll for trending carousel
    private Handler carouselHandler;
    private Runnable carouselRunnable;
    private int currentCarouselPosition = 0;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setupToolbar();
        setupTrendingCarousel();
        setupRecyclerView();
        setupViewModel();
        setupCategoryChips();
        setupSwipeRefresh();
        setupSearchDebounce();
        
        // Load initial data
        loadNews();
        loadTrendingNews();
    }
    
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("🇮🇳 India News");
        }
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        // Refresh news when activity becomes visible
        if (!isSearchMode) {
            loadNews();
            loadTrendingNews();
        }
    }
    
    private void setupTrendingCarousel() {
        trendingAdapter = new TrendingNewsAdapter();
        binding.trendingViewPager.setAdapter(trendingAdapter);
        binding.trendingViewPager.setOffscreenPageLimit(1);
        
        // Add page margin
        int pageMarginPx = (int) (16 * getResources().getDisplayMetrics().density);
        int offsetPx = (int) (32 * getResources().getDisplayMetrics().density);
        binding.trendingViewPager.setPageTransformer((page, position) -> {
            float offset = position * -(2 * offsetPx + pageMarginPx);
            page.setTranslationX(offset);
        });
        
        // Click listener
        trendingAdapter.setOnTrendingClickListener(this::openArticleDetail);
        
        // Setup indicator - Use attachTo for ViewPager2 in DotsIndicator 5.0
        binding.trendingIndicator.attachTo(binding.trendingViewPager);
        
        // Auto-scroll setup
        carouselHandler = new Handler(Looper.getMainLooper());
        carouselRunnable = new Runnable() {
            @Override
            public void run() {
                if (trendingAdapter.getItemCount() > 0) {
                    currentCarouselPosition = (currentCarouselPosition + 1) % trendingAdapter.getItemCount();
                    binding.trendingViewPager.setCurrentItem(currentCarouselPosition, true);
                    carouselHandler.postDelayed(this, 4000);
                }
            }
        };
    }
    
    private void setupRecyclerView() {
        adapter = new NewsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        
        adapter.setOnArticleClickListener(this::openArticleDetail);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        
        // Observe articles
        viewModel.getArticles().observe(this, articles -> {
            if (articles != null) {
                adapter.setArticles(articles);
                binding.emptyView.setVisibility(articles.isEmpty() ? View.VISIBLE : View.GONE);
            }
        });
        
        // Observe loading
        viewModel.getIsLoading().observe(this, isLoading -> {
            if (isLoading != null) {
                binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
            }
        });
        
        // Observe errors
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
                binding.errorView.setVisibility(View.VISIBLE);
                binding.errorText.setText(error);
            } else {
                binding.errorView.setVisibility(View.GONE);
            }
        });
        
        // Observe refresh
        viewModel.getIsRefreshing().observe(this, isRefreshing -> {
            if (isRefreshing != null) {
                binding.swipeRefresh.setRefreshing(isRefreshing);
            }
        });
        
        // Observe trending
        viewModel.getTrendingArticles().observe(this, articles -> {
            if (articles != null && !articles.isEmpty()) {
                trendingAdapter.setTrendingArticles(articles);
                binding.trendingContainer.setVisibility(View.VISIBLE);
                // Start auto-scroll
                carouselHandler.removeCallbacks(carouselRunnable);
                carouselHandler.postDelayed(carouselRunnable, 4000);
            } else {
                binding.trendingContainer.setVisibility(View.GONE);
            }
        });
    }
    
    private void setupCategoryChips() {
        binding.chipAll.setOnClickListener(v -> selectCategory(Constants.CATEGORY_TOP));
        binding.chipBusiness.setOnClickListener(v -> selectCategory(Constants.CATEGORY_BUSINESS));
        binding.chipTechnology.setOnClickListener(v -> selectCategory(Constants.CATEGORY_TECHNOLOGY));
        binding.chipSports.setOnClickListener(v -> selectCategory(Constants.CATEGORY_SPORTS));
        binding.chipHealth.setOnClickListener(v -> selectCategory(Constants.CATEGORY_HEALTH));
        binding.chipEntertainment.setOnClickListener(v -> selectCategory(Constants.CATEGORY_ENTERTAINMENT));
        binding.chipPolitics.setOnClickListener(v -> selectCategory(Constants.CATEGORY_POLITICS));
        
        // World news chip
        binding.chipWorld.setOnClickListener(v -> {
            currentCountry = Constants.COUNTRY_WORLD;
            selectCategory(Constants.CATEGORY_TOP);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("🌍 World News");
            }
        });
        
        binding.retryButton.setOnClickListener(v -> loadNews());
    }
    
    private void setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (isSearchMode) {
                binding.swipeRefresh.setRefreshing(false);
            } else {
                viewModel.refresh();
                loadTrendingNews();
            }
        });
    }
    
    private void setupSearchDebounce() {
        searchHandler = new Handler(Looper.getMainLooper());
    }
    
    private void selectCategory(String category) {
        currentCategory = category;
        currentCountry = Constants.COUNTRY_INDIA;
        isSearchMode = false;
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("🇮🇳 India News");
        }
        
        // Update chip selection
        uncheckAllChips();
        
        Chip selectedChip = null;
        switch (category) {
            case Constants.CATEGORY_BUSINESS:
                selectedChip = binding.chipBusiness;
                break;
            case Constants.CATEGORY_TECHNOLOGY:
                selectedChip = binding.chipTechnology;
                break;
            case Constants.CATEGORY_SPORTS:
                selectedChip = binding.chipSports;
                break;
            case Constants.CATEGORY_HEALTH:
                selectedChip = binding.chipHealth;
                break;
            case Constants.CATEGORY_ENTERTAINMENT:
                selectedChip = binding.chipEntertainment;
                break;
            case Constants.CATEGORY_POLITICS:
                selectedChip = binding.chipPolitics;
                break;
            default:
                selectedChip = binding.chipAll;
                break;
        }
        
        if (selectedChip != null) {
            selectedChip.setChecked(true);
        }
        
        loadNews();
    }
    
    private void uncheckAllChips() {
        binding.chipAll.setChecked(false);
        binding.chipBusiness.setChecked(false);
        binding.chipTechnology.setChecked(false);
        binding.chipSports.setChecked(false);
        binding.chipHealth.setChecked(false);
        binding.chipEntertainment.setChecked(false);
        binding.chipPolitics.setChecked(false);
        binding.chipWorld.setChecked(false);
    }
    
    private void loadNews() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            binding.errorView.setVisibility(View.VISIBLE);
            binding.errorText.setText("No internet connection");
            return;
        }
        
        binding.errorView.setVisibility(View.GONE);
        viewModel.fetchNews(currentCountry, currentCategory, currentLanguage, "india");
    }
    
    private void loadTrendingNews() {
        viewModel.fetchTrendingNews(Constants.COUNTRY_INDIA, currentLanguage);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        
        if (searchView != null) {
            searchView.setQueryHint("Search news...");
            
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    performSearch(query);
                    return true;
                }
                
                @Override
                public boolean onQueryTextChange(String newText) {
                    if (searchRunnable != null) {
                        searchHandler.removeCallbacks(searchRunnable);
                    }
                    
                    searchRunnable = () -> {
                        if (newText.length() >= 3) {
                            performSearch(newText);
                        }
                    };
                    
                    searchHandler.postDelayed(searchRunnable, 500);
                    return true;
                }
            });
            
            searchView.setOnCloseListener(() -> {
                isSearchMode = false;
                loadNews();
                return false;
            });
        }
        
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
    
    private void performSearch(String query) {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        
        isSearchMode = true;
        uncheckAllChips();
        binding.errorView.setVisibility(View.GONE);
        viewModel.searchNews(query);
    }
    
    private void openArticleDetail(NewsArticle article) {
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra("article_link", article.getLink());
        intent.putExtra("article_title", article.getTitle());
        intent.putExtra("article_image", article.getImageUrl());
        intent.putExtra("article_author", article.getAuthor());
        intent.putExtra("article_date", article.getPubDate());
        intent.putExtra("article_content", article.getContent());
        intent.putExtra("article_description", article.getDescription());
        intent.putExtra("article_source", article.getSourceId());
        startActivity(intent);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        if (trendingAdapter.getItemCount() > 0) {
            carouselHandler.postDelayed(carouselRunnable, 4000);
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        carouselHandler.removeCallbacks(carouselRunnable);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
        carouselHandler.removeCallbacks(carouselRunnable);
    }
}
