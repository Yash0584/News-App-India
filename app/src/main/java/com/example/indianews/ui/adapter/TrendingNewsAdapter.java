package com.example.indianews.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.indianews.R;
import com.example.indianews.data.model.NewsArticle;
import java.util.ArrayList;
import java.util.List;

public class TrendingNewsAdapter extends RecyclerView.Adapter<TrendingNewsAdapter.TrendingViewHolder> {
    
    private List<NewsArticle> trendingArticles = new ArrayList<>();
    private OnTrendingClickListener listener;
    
    public interface OnTrendingClickListener {
        void onTrendingClick(NewsArticle article);
    }
    
    public void setOnTrendingClickListener(OnTrendingClickListener listener) {
        this.listener = listener;
    }
    
    public void setTrendingArticles(List<NewsArticle> articles) {
        this.trendingArticles = articles;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public TrendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trending_news, parent, false);
        return new TrendingViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull TrendingViewHolder holder, int position) {
        NewsArticle article = trendingArticles.get(position);
        holder.bind(article);
    }
    
    @Override
    public int getItemCount() {
        return Math.min(trendingArticles.size(), 5); // Max 5 trending articles
    }
    
    class TrendingViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView trendingImage;
        private final TextView trendingTitle;
        
        public TrendingViewHolder(@NonNull View itemView) {
            super(itemView);
            trendingImage = itemView.findViewById(R.id.trending_image);
            trendingTitle = itemView.findViewById(R.id.trending_title);
            
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onTrendingClick(trendingArticles.get(getAdapterPosition()));
                }
            });
        }
        
        public void bind(NewsArticle article) {
            trendingTitle.setText(article.getTitle());
            
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(article.getImageUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(trendingImage);
            } else {
                trendingImage.setImageResource(R.drawable.placeholder_image);
            }
        }
    }
}
