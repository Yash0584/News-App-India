package com.example.indianews.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.indianews.data.model.NewsArticle;
import java.util.List;

@Dao
public interface ArticleDao {
    
    /**
     * Insert article (for favorites)
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(NewsArticle article);
    
    /**
     * Update article
     */
    @Update
    void update(NewsArticle article);
    
    /**
     * Delete article
     */
    @Delete
    void delete(NewsArticle article);
    
    /**
     * Get all favorite articles
     */
    @Query("SELECT * FROM articles WHERE isFavorite = 1 ORDER BY savedTimestamp DESC")
    LiveData<List<NewsArticle>> getAllFavorites();
    
    /**
     * Check if article is favorited by link
     */
    @Query("SELECT * FROM articles WHERE link = :link AND isFavorite = 1 LIMIT 1")
    NewsArticle getFavoriteByLink(String link);
    
    /**
     * Delete favorite by link
     */
    @Query("DELETE FROM articles WHERE link = :link")
    void deleteFavoriteByLink(String link);
    
    /**
     * Delete all favorites
     */
    @Query("DELETE FROM articles WHERE isFavorite = 1")
    void deleteAllFavorites();
    
    /**
     * Get favorite count
     */
    @Query("SELECT COUNT(*) FROM articles WHERE isFavorite = 1")
    LiveData<Integer> getFavoriteCount();
}
