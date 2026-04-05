# 🇮🇳 India News Pro - Complete Setup Guide

## 📱 **App Features**

✅ **India-First News** - Primary focus on Indian news from 86,000+ sources
✅ **Trending Carousel** - Auto-scrolling top 5 trending stories
✅ **City-Specific News** - Select from 10 major Indian cities
✅ **World News Tab** - Secondary section for international news
✅ **Full Article View** - Read complete articles in-app (WebView)
✅ **Favorites System** - Save and manage your favorite articles
✅ **13 Indian Languages** - English, Hindi, Tamil, Telugu, and more
✅ **Material Design 3** - Beautiful, modern UI
✅ **Offline Support** - Favorites available offline

---

## 🔑 **Step 1: Get Your FREE NewsData.io API Key**

### **Why NewsData.io?**
- ✅ 2,000 articles/day (vs NewsAPI's 1,000)
- ✅ City-specific news for India
- ✅ Full article content (not just snippets)
- ✅ 13 Indian languages
- ✅ **FREE for commercial use**

### **Get API Key (2 minutes):**

1. **Go to:** https://newsdata.io/register
2. **Sign up** with your email
3. **Verify** your email
4. **Dashboard** → Copy your API key (looks like: `pub_12345abcdef...`)

---

## ⚙️ **Step 2: Add API Key to Project**

### **Open the project in Android Studio:**

1. Extract the zip file
2. **Android Studio → File → Open** → Select `IndiaNewsApp` folder
3. Wait for Gradle sync to complete

### **Add your API key:**

**Open:** `app/src/main/java/com/example/indianews/utils/Constants.java`

**Find this line:**
```java
public static final String NEWS_API_KEY = "YOUR_NEWSDATA_IO_API_KEY_HERE";
```

**Replace with your actual key:**
```java
public static final String NEWS_API_KEY = "pub_12345abcdef...";  // Your key here
```

**Save** the file (Ctrl+S)

---

## 🏗️ **Step 3: Build & Run**

1. **Build → Clean Project**
2. **Build → Rebuild Project**
3. **Click Run** ▶️ (or press Shift+F10)
4. **Select your device** (emulator or physical device)

**First launch:** App will load India's top news automatically!

---

## 🎯 **How to Use the App**

### **Home Screen:**
- **🔥 Trending Carousel** (top) - Auto-scrolls every 4 seconds
- **📍 City Selector** - Tap to choose your city
- **Category Chips** - Politics, Business, Tech, Sports, etc.
- **News Feed** - Scroll to see all articles

### **Reading Articles:**
1. Tap any article → See summary
2. Tap **"Read Full Article"** → Full content loads in WebView
3. Tap **"Add to Favorites" ⭐** → Save for later

### **Favorites:**
- Top menu → **Favorites icon** ⭐
- All saved articles available offline

### **World News:**
- Tap **🌍 World** chip → Switch to international news

### **Search:**
- Top menu → **Search icon** 🔍
- Type keyword → Results appear

---

## 📊 **Project Structure**

```
IndiaNewsApp/
├── data/
│   ├── model/          # NewsArticle, NewsResponse, City
│   ├── remote/         # NewsData.io API service
│   ├── local/          # Room database for favorites
│   └── repository/     # Data layer
├── ui/
│   ├── main/           # MainActivity with news feed
│   ├── detail/         # ArticleDetailActivity
│   ├── favorites/      # FavoritesActivity
│   └── adapters/       # RecyclerView adapters
├── viewmodel/          # MVVM ViewModels
└── utils/              # Constants, DateUtils, NetworkUtils
```

---

## 🎨 **Customization Options**

### **Change Default Language:**
In `Constants.java`:
```java
public static final String LANGUAGE_ENGLISH = "en";  // Change to "hi" for Hindi
```

### **Add More Cities:**
In `Constants.java` → `getIndianCities()`:
```java
cities.add(new City("Lucknow", "lucknow", "🏛️"));
```

### **Change Theme Colors:**
`app/src/main/res/values/colors.xml` → Edit color values

---

## 🐛 **Troubleshooting**

### **"No news available"**
✅ Check your API key is correct
✅ Check internet connection
✅ Free plan has 12-hour delay (news from yesterday)

### **Build errors**
✅ **Build → Clean Project**
✅ **File → Invalidate Caches / Restart**
✅ Make sure JDK 11 is selected in gradle.properties

### **App crashes on launch**
✅ Check API key is not "YOUR_NEWSDATA_IO_API_KEY_HERE"
✅ Check internet permission in AndroidManifest.xml

---

## 📈 **API Limits (Free Plan)**

| Feature | Limit |
|---------|-------|
| Daily Credits | 200 |
| Articles per credit | 10 |
| Total articles/day | 2,000 |
| News delay | 12 hours |
| Commercial use | ✅ Allowed |

**Need more?** Upgrade to paid plan at https://newsdata.io/pricing

---

## 🚀 **Next Steps (Optional Enhancements)**

1. **Add Hindi Language Toggle** - Switch between English/Hindi
2. **Push Notifications** - Get notified of breaking news
3. **Share Feature** - Share articles to social media
4. **Dark Mode** - Night theme support
5. **Bookmarks Sync** - Cloud backup of favorites

---

## 📝 **Important Notes**

⚠️ **API Key Security:**
- Never commit your API key to GitHub
- Use local.properties for production apps

⚠️ **Free Plan Limitations:**
- 12-hour news delay (perfect for learning/testing)
- 200 API calls per day

✅ **Commercial Use:** Free plan allows commercial use!

---

## 🎓 **Learning Resources**

- **NewsData.io Docs:** https://newsdata.io/documentation
- **Android MVVM:** https://developer.android.com/topic/architecture
- **Room Database:** https://developer.android.com/training/data-storage/room
- **Retrofit:** https://square.github.io/retrofit/

---

## 📧 **Support**

**Issues with NewsData.io API?**
→ https://newsdata.io/contact

**Android Development Questions?**
→ https://stackoverflow.com/questions/tagged/android

---

## 🎉 **You're All Set!**

Your India News Pro app is ready to use! Enjoy staying updated with news from across India! 🇮🇳

**Happy Coding!** 🚀
