package com.example.indianews.data.model;

public class City {
    private String name;
    private String code;
    private String emoji;
    private boolean isSelected;
    
    public City(String name, String code, String emoji) {
        this.name = name;
        this.code = code;
        this.emoji = emoji;
        this.isSelected = false;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getEmoji() {
        return emoji;
    }
    
    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }
    
    public boolean isSelected() {
        return isSelected;
    }
    
    public void setSelected(boolean selected) {
        isSelected = selected;
    }
    
    public String getDisplayName() {
        return emoji + " " + name;
    }
}
