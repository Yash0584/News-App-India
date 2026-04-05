package com.example.indianews.ui.detail;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.bumptech.glide.Glide;
import com.example.indianews.R;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.databinding.ActivityDetailBinding;
import com.example.indianews.utils.DateFormatUtils;
import com.example.indianews.viewmodel.FavoritesViewModel;

public class DetailActivity extends AppCompatActivity {
    
    private ActivityDetailBinding binding;
    private FavoritesViewModel favoritesViewModel;
    private NewsArticle currentArticle;
    private boolean isFavorite = false;
    private boolean isWebViewMode = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setupToolbar();
        setupViewModel();
        loadArticleData();
        setupButtons();
        setupWebView();
    }
    
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }
    
    private void setupViewModel() {
        favoritesViewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
    }
    
    private void loadArticleData() {
        android.content.Intent intent = getIntent();
        
        String link = intent.getStringExtra("article_link");
        String title = intent.getStringExtra("article_title");
        String imageUrl = intent.getStringExtra("article_image");
        String author = intent.getStringExtra("article_author");
        String pubDate = intent.getStringExtra("article_date");
        String content = intent.getStringExtra("article_content");
        String description = intent.getStringExtra("article_description");
        String sourceId = intent.getStringExtra("article_source");
        
        // Create article object
        currentArticle = new NewsArticle();
        currentArticle.setLink(link);
        currentArticle.setTitle(title);
        currentArticle.setImageUrl(imageUrl);
        currentArticle.setPubDate(pubDate);
        currentArticle.setContent(content);
        currentArticle.setDescription(description);
        currentArticle.setSourceId(sourceId);
        
        // Display data
        displayArticle();
        
        // Check if bookmarked
        checkFavoriteStatus();
    }
    
    private void displayArticle() {
        // Title
        binding.titleTextView.setText(currentArticle.getTitle());
        
        // Author and date
        String authorDate = "";
        if (currentArticle.getAuthor() != null && !currentArticle.getAuthor().isEmpty()) {
            authorDate = currentArticle.getAuthor() + " • ";
        }
        authorDate += DateFormatUtils.getFullDateTime(currentArticle.getPubDate());
        binding.authorDateTextView.setText(authorDate);
        
        // Source
        if (currentArticle.getSourceId() != null) {
            binding.sourceTextView.setText(currentArticle.getSourceId());
        }
        
        // Description
        if (currentArticle.getDescription() != null && !currentArticle.getDescription().isEmpty()) {
            binding.descriptionTextView.setText(currentArticle.getDescription());
            binding.descriptionTextView.setVisibility(View.VISIBLE);
        } else {
            binding.descriptionTextView.setVisibility(View.GONE);
        }
        
        // Content
        if (currentArticle.getContent() != null && !currentArticle.getContent().isEmpty()) {
            binding.contentTextView.setText(currentArticle.getContent());
            binding.contentTextView.setVisibility(View.VISIBLE);
        } else {
            binding.contentTextView.setVisibility(View.GONE);
        }
        
        // Image
        if (currentArticle.getImageUrl() != null && !currentArticle.getImageUrl().isEmpty()) {
            Glide.with(this)
                    .load(currentArticle.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .error(R.drawable.placeholder_image)
                    .centerCrop()
                    .into(binding.headerImage);
        } else {
            binding.headerImage.setImageResource(R.drawable.placeholder_image);
        }
    }
    
    private void setupWebView() {
        WebSettings webSettings = binding.articleWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        
        binding.articleWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.webViewProgress.setVisibility(View.GONE);
            }
        });
    }
    
    private void setupButtons() {
        // Read Full Article button
        binding.readFullButton.setOnClickListener(v -> {
            if (currentArticle.getLink() != null && !currentArticle.getLink().isEmpty()) {
                toggleWebView();
            }
        });
        
        // Favorite button
        binding.favoriteButton.setOnClickListener(v -> toggleFavorite());
    }
    
    private void toggleWebView() {
        if (isWebViewMode) {
            // Show article content
            binding.articleContent.setVisibility(View.VISIBLE);
            binding.webViewContainer.setVisibility(View.GONE);
            binding.readFullButton.setText("Read Full Article");
            binding.readFullButton.setIconResource(R.drawable.ic_open_in_browser);
            isWebViewMode = false;
        } else {
            // Show WebView with full article
            binding.articleContent.setVisibility(View.GONE);
            binding.webViewContainer.setVisibility(View.VISIBLE);
            binding.webViewProgress.setVisibility(View.VISIBLE);
            binding.articleWebView.loadUrl(currentArticle.getLink());
            binding.readFullButton.setText("Show Summary");
            binding.readFullButton.setIconResource(R.drawable.ic_arrow_back);
            isWebViewMode = true;
        }
    }
    
    private void checkFavoriteStatus() {
        if (currentArticle.getLink() != null) {
            favoritesViewModel.isArticleFavorite(currentArticle.getLink(), favorite -> {
                runOnUiThread(() -> {
                    isFavorite = favorite;
                    updateFavoriteButton();
                });
            });
        }
    }
    
    private void toggleFavorite() {
        if (isFavorite) {
            favoritesViewModel.removeFavorite(currentArticle.getLink());
            isFavorite = false;
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
        } else {
            favoritesViewModel.addFavorite(currentArticle);
            isFavorite = true;
            Toast.makeText(this, "Added to favorites ⭐", Toast.LENGTH_SHORT).show();
        }
        updateFavoriteButton();
    }
    
    private void updateFavoriteButton() {
        if (isFavorite) {
            binding.favoriteButton.setIconResource(R.drawable.ic_bookmark_filled);
            binding.favoriteButton.setText("Favorited");
        } else {
            binding.favoriteButton.setIconResource(R.drawable.ic_bookmark_border);
            binding.favoriteButton.setText("Add to Favorites");
        }
    }
    
    @Override
    public void onBackPressed() {
        if (binding.articleWebView.canGoBack() && isWebViewMode) {
            binding.articleWebView.goBack();
        } else if (isWebViewMode) {
            toggleWebView();
        } else {
            super.onBackPressed();
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
