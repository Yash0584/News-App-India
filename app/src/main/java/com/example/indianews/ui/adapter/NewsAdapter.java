package com.example.indianews.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.indianews.R;
import com.example.indianews.data.model.NewsArticle;
import com.example.indianews.utils.DateFormatUtils;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    
    private List<NewsArticle> articles = new ArrayList<>();
    private OnArticleClickListener listener;
    
    public interface OnArticleClickListener {
        void onArticleClick(NewsArticle article);
    }
    
    public void setOnArticleClickListener(OnArticleClickListener listener) {
        this.listener = listener;
    }
    
    public void setArticles(List<NewsArticle> newArticles) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return articles.size();
            }
            
            @Override
            public int getNewListSize() {
                return newArticles.size();
            }
            
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return articles.get(oldItemPosition).getArticleId() != null &&
                       articles.get(oldItemPosition).getArticleId().equals(
                               newArticles.get(newItemPosition).getArticleId()
                       );
            }
            
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                NewsArticle oldArticle = articles.get(oldItemPosition);
                NewsArticle newArticle = newArticles.get(newItemPosition);
                return oldArticle.getTitle().equals(newArticle.getTitle());
            }
        });
        
        articles = newArticles;
        diffResult.dispatchUpdatesTo(this);
    }
    
    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = articles.get(position);
        holder.bind(article);
    }
    
    @Override
    public int getItemCount() {
        return articles.size();
    }
    
    class NewsViewHolder extends RecyclerView.ViewHolder {
        
        private final ImageView imageView;
        private final TextView titleTextView;
        private final TextView sourceTextView;
        private final TextView dateTextView;
        private final TextView categoryBadge;
        
        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.article_image);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            dateTextView = itemView.findViewById(R.id.article_date);
            categoryBadge = itemView.findViewById(R.id.category_badge);
            
            itemView.setOnClickListener(v -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onArticleClick(articles.get(getAdapterPosition()));
                }
            });
        }
        
        public void bind(NewsArticle article) {
            // Title
            titleTextView.setText(article.getTitle());
            
            // Source
            sourceTextView.setText(article.getSourceId() != null ? article.getSourceId() : "Unknown");
            
            // Date
            dateTextView.setText(DateFormatUtils.getTimeAgo(article.getPubDate()));
            
            // Category badge
            if (article.getCategory() != null && !article.getCategory().isEmpty()) {
                categoryBadge.setText(article.getCategoryDisplay());
                categoryBadge.setVisibility(View.VISIBLE);
            } else {
                categoryBadge.setVisibility(View.GONE);
            }
            
            // Image
            if (article.getImageUrl() != null && !article.getImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(article.getImageUrl())
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                        .centerCrop()
                        .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder_image);
            }
        }
    }
}
