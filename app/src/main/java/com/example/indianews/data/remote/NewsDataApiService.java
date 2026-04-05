package com.example.indianews.data.remote;

import com.example.indianews.data.model.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * NewsData.io API Service
 * Documentation: https://newsdata.io/documentation
 */
public interface NewsDataApiService {
    
    /**
     * Get latest news
     * @param apiKey Your NewsData.io API key
     * @param country Country code (in = India, us = USA, etc.)
     * @param category Category (top, business, technology, sports, entertainment, health, science, politics)
     * @param language Language code (en, hi for Hindi, etc.)
     * @param q Search query (optional)
     * @param qInTitle Search in title only (optional)
     * @param domain Specific domain to search from (optional)
     * @param page Page number for pagination (optional)
     * @return NewsResponse with articles
     */
    @GET("news")
    Call<NewsResponse> getLatestNews(
            @Query("apikey") String apiKey,
            @Query("country") String country,
            @Query("category") String category,
            @Query("language") String language,
            @Query("q") String query,
            @Query("qInTitle") String queryInTitle,
            @Query("domain") String domain,
            @Query("page") String page
    );
    
    /**
     * Get news by specific query (search)
     */
    @GET("news")
    Call<NewsResponse> searchNews(
            @Query("apikey") String apiKey,
            @Query("q") String query,
            @Query("country") String country,
            @Query("language") String language,
            @Query("page") String page
    );
    
    /**
     * Get trending/top news for India
     */
    @GET("news")
    Call<NewsResponse> getTrendingNews(
            @Query("apikey") String apiKey,
            @Query("country") String country,
            @Query("language") String language,
            @Query("prioritydomain") String priorityDomain
    );
}
