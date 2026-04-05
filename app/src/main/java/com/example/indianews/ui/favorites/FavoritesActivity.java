package com.example.indianews.ui.favorites;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.indianews.R;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.databinding.ActivityFavoritesBinding;
import com.example.indianews.ui.adapter.NewsAdapter;
import com.example.indianews.ui.detail.DetailActivity;
import com.example.indianews.viewmodel.FavoritesViewModel;

public class FavoritesActivity extends AppCompatActivity {
    
    private ActivityFavoritesBinding binding;
    private FavoritesViewModel viewModel;
    private NewsAdapter adapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setupToolbar();
        setupRecyclerView();
        setupViewModel();
        setupClearButton();
    }
    
    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("⭐ Favorites");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }
    
    private void setupRecyclerView() {
        adapter = new NewsAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(adapter);
        
        adapter.setOnArticleClickListener(this::openArticleDetail);
    }
    
    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(FavoritesViewModel.class);
        
        viewModel.getAllFavorites().observe(this, favorites -> {
            if (favorites != null) {
                adapter.setArticles(favorites);
                
                if (favorites.isEmpty()) {
                    binding.emptyView.setVisibility(View.VISIBLE);
                    binding.recyclerView.setVisibility(View.GONE);
                    binding.clearButton.setVisibility(View.GONE);
                } else {
                    binding.emptyView.setVisibility(View.GONE);
                    binding.recyclerView.setVisibility(View.VISIBLE);
                    binding.clearButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    
    private void setupClearButton() {
        binding.clearButton.setOnClickListener(v -> showClearConfirmationDialog());
    }
    
    private void showClearConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Clear All Favorites")
                .setMessage("Are you sure you want to remove all favorite articles?")
                .setPositiveButton("Clear", (dialog, which) -> {
                    viewModel.clearAllFavorites();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void openArticleDetail(NewsArticle article) {
        Intent intent = new Intent(FavoritesActivity.this, DetailActivity.class);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
