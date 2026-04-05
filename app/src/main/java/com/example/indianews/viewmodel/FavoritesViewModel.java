package com.example.indianews.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.data.repository.NewsRepository;
import java.util.List;

public class FavoritesViewModel extends AndroidViewModel {
    
    private final NewsRepository repository;
    private final LiveData<List<NewsArticle>> favoritesLiveData;
    private final LiveData<Integer> favoriteCountLiveData;
    
    public FavoritesViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(application);
        favoritesLiveData = repository.getAllFavorites();
        favoriteCountLiveData = repository.getFavoriteCount();
    }
    
    /**
     * Add article to favorites
     */
    public void addFavorite(NewsArticle article) {
        repository.saveFavorite(article);
    }
    
    /**
     * Remove article from favorites
     */
    public void removeFavorite(String link) {
        repository.removeFavorite(link);
    }
    
    /**
     * Check if article is favorite
     */
    public void isArticleFavorite(String link, NewsRepository.FavoriteCheckCallback callback) {
        repository.isArticleFavorite(link, callback);
    }
    
    /**
     * Clear all favorites
     */
    public void clearAllFavorites() {
        repository.clearAllFavorites();
    }
    
    // Getters
    public LiveData<List<NewsArticle>> getAllFavorites() {
        return favoritesLiveData;
    }
    
    public LiveData<Integer> getFavoriteCount() {
        return favoriteCountLiveData;
    }
}
