package com.example.indianews.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.data.model.NewsResponse;
import com.example.indianews.data.repository.NewsRepository;
import com.example.indianews.utils.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    
    private final NewsRepository repository;
    private final MutableLiveData<List<NewsArticle>> articlesLiveData;
    private final MutableLiveData<List<NewsArticle>> trendingArticlesLiveData;
    private final MutableLiveData<Boolean> isLoading;
    private final MutableLiveData<String> errorMessage;
    private final MutableLiveData<Boolean> isRefreshing;
    
    private String currentCountry = Constants.COUNTRY_INDIA;
    private String currentCategory = Constants.CATEGORY_TOP;
    private String currentLanguage = Constants.LANGUAGE_ENGLISH;
    private String currentCity = "india";
    
    public NewsViewModel(@NonNull Application application) {
        super(application);
        repository = new NewsRepository(application);
        articlesLiveData = new MutableLiveData<>();
        trendingArticlesLiveData = new MutableLiveData<>();
        isLoading = new MutableLiveData<>();
        errorMessage = new MutableLiveData<>();
        isRefreshing = new MutableLiveData<>();
    }
    
    /**
     * Fetch latest news
     */
    public void fetchNews(String country, String category, String language, String city) {
        this.currentCountry = country;
        this.currentCategory = category;
        this.currentLanguage = language;
        this.currentCity = city;
        
        isLoading.setValue(true);
        errorMessage.setValue(null);
        
        // Force a UI clear if refreshing to show that work is being done
        if (Boolean.TRUE.equals(isRefreshing.getValue())) {
            articlesLiveData.setValue(new ArrayList<>());
        }
        
        repository.getLatestNews(country, category, language, city)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        isLoading.setValue(false);
                        isRefreshing.setValue(false);
                        
                        if (response.isSuccessful() && response.body() != null) {
                            NewsResponse newsResponse = response.body();
                            if (newsResponse.getResults() != null && !newsResponse.getResults().isEmpty()) {
                                List<NewsArticle> results = newsResponse.getResults();
                                List<NewsArticle> filteredResults = new ArrayList<>();
                                for (NewsArticle article : results) {
                                    if (article.getTitle() != null && !article.getTitle().isEmpty() && 
                                        article.getLink() != null && !article.getLink().isEmpty()) {
                                        filteredResults.add(article);
                                    }
                                }
                                
                                if (!filteredResults.isEmpty()) {
                                    articlesLiveData.setValue(filteredResults);
                                } else {
                                    handleEmptyResults(country, category, language, city);
                                }
                            } else {
                                handleEmptyResults(country, category, language, city);
                            }
                        } else {
                            errorMessage.setValue("Failed to load news. Please try again.");
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        isLoading.setValue(false);
                        isRefreshing.setValue(false);
                        errorMessage.setValue("Network error. Please check your connection.");
                    }
                });
    }
    
    private void handleEmptyResults(String country, String category, String language, String city) {
        if (!city.equals("india")) {
            fetchNews(country, category, language, "india");
        } else if (!category.equals(Constants.CATEGORY_TOP)) {
            fetchNews(country, Constants.CATEGORY_TOP, language, "india");
        } else {
            articlesLiveData.setValue(new ArrayList<>());
            errorMessage.setValue("No news available for this selection");
        }
    }
    
    /**
     * Fetch trending news for carousel
     */
    public void fetchTrendingNews(String country, String language) {
        repository.getTrendingNews(country, language)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            NewsResponse newsResponse = response.body();
                            if (newsResponse.getResults() != null && !newsResponse.getResults().isEmpty()) {
                                List<NewsArticle> results = newsResponse.getResults();
                                List<NewsArticle> filteredTrending = new ArrayList<>();
                                for (NewsArticle article : results) {
                                    if (article.getTitle() != null && !article.getTitle().isEmpty()) {
                                        filteredTrending.add(article);
                                    }
                                }
                                
                                if (!filteredTrending.isEmpty()) {
                                    List<NewsArticle> trending = filteredTrending.subList(
                                            0, Math.min(5, filteredTrending.size())
                                    );
                                    trendingArticlesLiveData.setValue(trending);
                                }
                            }
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                    }
                });
    }
    
    /**
     * Search news
     */
    public void searchNews(String query) {
        isLoading.setValue(true);
        errorMessage.setValue(null);
        articlesLiveData.setValue(new ArrayList<>()); // Clear before search
        
        repository.searchNews(query, currentCountry, currentLanguage)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        isLoading.setValue(false);
                        
                        if (response.isSuccessful() && response.body() != null) {
                            NewsResponse newsResponse = response.body();
                            if (newsResponse.getResults() != null && !newsResponse.getResults().isEmpty()) {
                                articlesLiveData.setValue(newsResponse.getResults());
                            } else {
                                articlesLiveData.setValue(new ArrayList<>());
                                errorMessage.setValue("No results found for \"" + query + "\"");
                            }
                        } else {
                            errorMessage.setValue("Search failed. Please try again.");
                        }
                    }
                    
                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        isLoading.setValue(false);
                        errorMessage.setValue("Network error. Please check your connection.");
                    }
                });
    }
    
    /**
     * Refresh current news
     */
    public void refresh() {
        isRefreshing.setValue(true);
        fetchNews(currentCountry, currentCategory, currentLanguage, currentCity);
    }
    
    // Getters
    public LiveData<List<NewsArticle>> getArticles() {
        return articlesLiveData;
    }
    
    public LiveData<List<NewsArticle>> getTrendingArticles() {
        return trendingArticlesLiveData;
    }
    
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    public LiveData<Boolean> getIsRefreshing() {
        return isRefreshing;
    }
}
