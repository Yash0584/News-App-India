package com.example.indianews.data.repository;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.indianews.data.local.AppDatabase;
import com.example.indianews.data.local.ArticleDao;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.data.model.NewsResponse;
import com.example.indianews.data.remote.NewsDataApiService;
import com.example.indianews.data.remote.RetrofitClient;
import com.example.indianews.utils.Constants;
import retrofit2.Call;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsRepository {
    
    private final NewsDataApiService apiService;
    private final ArticleDao articleDao;
    private final ExecutorService executorService;
    
    public NewsRepository(Context context) {
        apiService = RetrofitClient.getApiService();
        AppDatabase database = AppDatabase.getInstance(context);
        articleDao = database.articleDao();
        executorService = Executors.newSingleThreadExecutor();
    }
    
    /**
     * Get latest news from API
     */
    public Call<NewsResponse> getLatestNews(String country, String category, String language, String city) {
        String query = city != null && !city.equals("india") ? city : null;
        return apiService.getLatestNews(
                Constants.NEWS_API_KEY,
                country,
                category.isEmpty() ? null : category,
                language,
                query,
                null,
                null,
                null
        );
    }
    
    /**
     * Get trending news
     */
    public Call<NewsResponse> getTrendingNews(String country, String language) {
        return apiService.getTrendingNews(
                Constants.NEWS_API_KEY,
                country,
                language,
                null
        );
    }
    
    /**
     * Search news
     */
    public Call<NewsResponse> searchNews(String query, String country, String language) {
        return apiService.searchNews(
                Constants.NEWS_API_KEY,
                query,
                country,
                language,
                null
        );
    }
    
    /**
     * Save article as favorite
     */
    public void saveFavorite(NewsArticle article) {
        executorService.execute(() -> {
            article.setFavorite(true);
            article.setSavedTimestamp(System.currentTimeMillis());
            articleDao.insert(article);
        });
    }
    
    /**
     * Remove favorite
     */
    public void removeFavorite(String link) {
        executorService.execute(() -> {
            articleDao.deleteFavoriteByLink(link);
        });
    }
    
    /**
     * Get all favorites
     */
    public LiveData<List<NewsArticle>> getAllFavorites() {
        return articleDao.getAllFavorites();
    }
    
    /**
     * Check if article is favorite (async callback)
     */
    public void isArticleFavorite(String link, FavoriteCheckCallback callback) {
        executorService.execute(() -> {
            NewsArticle article = articleDao.getFavoriteByLink(link);
            callback.onResult(article != null);
        });
    }
    
    /**
     * Get favorite count
     */
    public LiveData<Integer> getFavoriteCount() {
        return articleDao.getFavoriteCount();
    }
    
    /**
     * Clear all favorites
     */
    public void clearAllFavorites() {
        executorService.execute(() -> {
            articleDao.deleteAllFavorites();
        });
    }
    
    // Callback interface
    public interface FavoriteCheckCallback {
        void onResult(boolean isFavorite);
    }
}
