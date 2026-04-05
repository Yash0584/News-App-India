package com.example.indianews.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NewsResponse {
    
    private String status;
    
    private int totalResults;
    
    private List<NewsArticle> results;
    
    @SerializedName("nextPage")
    private String nextPage;
    
    public NewsResponse() {
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public int getTotalResults() {
        return totalResults;
    }
    
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
    
    public List<NewsArticle> getResults() {
        return results;
    }
    
    public void setResults(List<NewsArticle> results) {
        this.results = results;
    }
    
    public String getNextPage() {
        return nextPage;
    }
    
    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }
}
