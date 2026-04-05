package com.example.indianews.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import com.example.indianews.data.local.Converters;
import com.google.gson.annotations.SerializedName;
import java.util.List;

@Entity(tableName = "articles")
@TypeConverters(Converters.class)
public class NewsArticle {
    
    @PrimaryKey(autoGenerate = true)
    private int id;
    
    @SerializedName("article_id")
    private String articleId;
    
    private String title;
    
    private String link;
    
    private List<String> keywords;
    
    private List<String> creator;
    
    @SerializedName("video_url")
    private String videoUrl;
    
    private String description;
    
    private String content;
    
    @SerializedName("pubDate")
    private String pubDate;
    
    @SerializedName("image_url")
    private String imageUrl;
    
    @SerializedName("source_id")
    private String sourceId;
    
    @SerializedName("source_priority")
    private int sourcePriority;
    
    @SerializedName("source_url")
    private String sourceUrl;
    
    @SerializedName("source_icon")
    private String sourceIcon;
    
    private String language;
    
    private List<String> country;
    
    private List<String> category;
    
    @SerializedName("ai_tag")
    private String aiTag;
    
    private String sentiment;
    
    @SerializedName("sentiment_stats")
    private String sentimentStats;
    
    @SerializedName("ai_region")
    private String aiRegion;
    
    // For local favorites tracking
    private boolean isFavorite;
    private long savedTimestamp;
    
    // Constructors
    public NewsArticle() {
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getArticleId() {
        return articleId;
    }
    
    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getLink() {
        return link;
    }
    
    public void setLink(String link) {
        this.link = link;
    }
    
    public List<String> getKeywords() {
        return keywords;
    }
    
    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
    
    public List<String> getCreator() {
        return creator;
    }
    
    public void setCreator(List<String> creator) {
        this.creator = creator;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getPubDate() {
        return pubDate;
    }
    
    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getSourceId() {
        return sourceId;
    }
    
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
    
    public int getSourcePriority() {
        return sourcePriority;
    }
    
    public void setSourcePriority(int sourcePriority) {
        this.sourcePriority = sourcePriority;
    }
    
    public String getSourceUrl() {
        return sourceUrl;
    }
    
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }
    
    public String getSourceIcon() {
        return sourceIcon;
    }
    
    public void setSourceIcon(String sourceIcon) {
        this.sourceIcon = sourceIcon;
    }
    
    public String getLanguage() {
        return language;
    }
    
    public void setLanguage(String language) {
        this.language = language;
    }
    
    public List<String> getCountry() {
        return country;
    }
    
    public void setCountry(List<String> country) {
        this.country = country;
    }
    
    public List<String> getCategory() {
        return category;
    }
    
    public void setCategory(List<String> category) {
        this.category = category;
    }
    
    public String getAiTag() {
        return aiTag;
    }
    
    public void setAiTag(String aiTag) {
        this.aiTag = aiTag;
    }
    
    public String getSentiment() {
        return sentiment;
    }
    
    public void setSentiment(String sentiment) {
        this.sentiment = sentiment;
    }
    
    public String getSentimentStats() {
        return sentimentStats;
    }
    
    public void setSentimentStats(String sentimentStats) {
        this.sentimentStats = sentimentStats;
    }
    
    public String getAiRegion() {
        return aiRegion;
    }
    
    public void setAiRegion(String aiRegion) {
        this.aiRegion = aiRegion;
    }
    
    public boolean isFavorite() {
        return isFavorite;
    }
    
    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
    
    public long getSavedTimestamp() {
        return savedTimestamp;
    }
    
    public void setSavedTimestamp(long savedTimestamp) {
        this.savedTimestamp = savedTimestamp;
    }
    
    // Helper methods
    public String getAuthor() {
        if (creator != null && !creator.isEmpty()) {
            return creator.get(0);
        }
        return sourceId != null ? sourceId : "Unknown";
    }
    
    public String getCategoryDisplay() {
        if (category != null && !category.isEmpty()) {
            return category.get(0).substring(0, 1).toUpperCase() + category.get(0).substring(1);
        }
        return "General";
    }
}
