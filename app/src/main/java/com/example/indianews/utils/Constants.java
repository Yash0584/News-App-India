package com.example.indianews.utils;

import com.example.indianews.data.model.City;
import java.util.ArrayList;
import java.util.List;

public class Constants {
    
    // NewsData.io API Key - Get your free key from https://newsdata.io/
    public static final String NEWS_API_KEY = "Enter Your API key here";
    
    // For UI testing with dummy key (limited functionality)
    public static final String DUMMY_API_KEY = "pub_dummy_key_for_ui_testing";
    
    // Country codes
    public static final String COUNTRY_INDIA = "in";
    public static final String COUNTRY_WORLD = "us"; // Using US as proxy for world news
    
    // Languages
    public static final String LANGUAGE_ENGLISH = "en";
    public static final String LANGUAGE_HINDI = "hi";
    
    // Categories
    public static final String CATEGORY_TOP = "top";
    public static final String CATEGORY_BUSINESS = "business";
    public static final String CATEGORY_TECHNOLOGY = "technology";
    public static final String CATEGORY_SPORTS = "sports";
    public static final String CATEGORY_ENTERTAINMENT = "entertainment";
    public static final String CATEGORY_HEALTH = "health";
    public static final String CATEGORY_SCIENCE = "science";
    public static final String CATEGORY_POLITICS = "politics";
    
    // Priority domains for India (major news sources)
    public static final String PRIORITY_DOMAINS_INDIA = "timesofindia,thehindu,hindustantimes,indianexpress,ndtv";
    
    /**
     * Get list of major Indian cities
     */
    public static List<City> getIndianCities() {
        List<City> cities = new ArrayList<>();
        cities.add(new City("All India", "india", "🇮🇳"));
        cities.add(new City("Mumbai", "mumbai", "🏙️"));
        cities.add(new City("Delhi", "delhi", "🏛️"));
        cities.add(new City("Bangalore", "bangalore", "💻"));
        cities.add(new City("Hyderabad", "hyderabad", "🏰"));
        cities.add(new City("Chennai", "chennai", "🌴"));
        cities.add(new City("Kolkata", "kolkata", "🎭"));
        cities.add(new City("Pune", "pune", "🎓"));
        cities.add(new City("Ahmedabad", "ahmedabad", "🏭"));
        cities.add(new City("Jaipur", "jaipur", "🕌"));
        return cities;
    }
    
    /**
     * Get category display name with emoji
     */
    public static String getCategoryDisplay(String category) {
        switch (category) {
            case CATEGORY_TOP:
                return "📰 Top Stories";
            case CATEGORY_BUSINESS:
                return "💼 Business";
            case CATEGORY_TECHNOLOGY:
                return "💻 Technology";
            case CATEGORY_SPORTS:
                return "⚽ Sports";
            case CATEGORY_ENTERTAINMENT:
                return "🎬 Entertainment";
            case CATEGORY_HEALTH:
                return "🏥 Health";
            case CATEGORY_SCIENCE:
                return "🔬 Science";
            case CATEGORY_POLITICS:
                return "🏛️ Politics";
            default:
                return "📰 News";
        }
    }
}
