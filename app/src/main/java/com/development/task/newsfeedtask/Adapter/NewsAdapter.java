package com.development.task.newsfeedtask.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.development.task.newsfeedtask.R;
import com.development.task.newsfeedtask.model.Article;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsFeedViewHolder>  {

        private Context mCtx;
        private List<Article> articleList;
        private Article newsFeed;
        private NewsAdapterClickListener adapterClickListener;
        private Resources resources;

        public NewsAdapter(Context mCtx, List<Article> newsFeedList, NewsAdapterClickListener adapterClickListener, Resources resources) {
            this.mCtx = mCtx;
            this.articleList = newsFeedList;
            this.adapterClickListener = adapterClickListener;
            this.resources = resources;
        }

        @Override
        public NewsAdapter.NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mCtx).inflate(R.layout.item_news_feed, parent, false);
            return new NewsAdapter.NewsFeedViewHolder(view);
        }

        @SuppressLint({"ResourceAsColor", "RestrictedApi"})
        @Override
        public void onBindViewHolder(NewsAdapter.NewsFeedViewHolder holder, int position) {
            Article article = articleList.get(position);
            holder.textviewarticletitle.setText(article.getTitle());
            holder.textViewArticleAuthor.setText(article.getDescription());
            //
            DateFormat dateFormat = new SimpleDateFormat("dd-MMMM-yyyy");
            Date date = null;//You will get date object relative to server/client timezone wherever it is parsed
            try {
                date = dateFormat.parse(article.getPublishedAt());
                DateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                String dateStr = formatter.format(date);
                holder.textViewArticlePublishedTime.setText(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Glide.with(mCtx).load(article.getUrlToImage()).placeholder(resources.getDrawable(R.drawable.placeholder)).into(holder.articleImageView);


        }



        @Override
        public int getItemCount() {
            return articleList.size();
        }

        class NewsFeedViewHolder extends RecyclerView.ViewHolder {

            TextView textviewarticletitle, textViewArticleAuthor,textViewArticlePublishedTime;
            ImageView articleImageView;
            CardView articleContainer;
            public NewsFeedViewHolder(View itemView) {
                super(itemView);
                textviewarticletitle = itemView.findViewById(R.id.title);
                textViewArticleAuthor = itemView.findViewById(R.id.author);
                textViewArticlePublishedTime = itemView.findViewById(R.id.publish_date);
                articleImageView = itemView.findViewById(R.id.article_image);
                articleContainer = itemView.findViewById(R.id.news_feed_container);
                articleContainer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapterClickListener.onArticleItemClicked(articleList.get(getAdapterPosition()));
                    }
                });

            }


        }




}
