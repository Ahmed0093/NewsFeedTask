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
import com.development.task.newsfeedtask.helper.DateHelper;
import com.development.task.newsfeedtask.model.Article;
import com.development.task.newsfeedtask.model.WeatherModel;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.NewsFeedViewHolder> {

    private Context mCtx;
    private List<WeatherModel> articleList;
    private Article newsFeed;
    private NewsAdapterClickListener adapterClickListener;
    private Resources resources;

    public ImageAdapter(Context mCtx, List<WeatherModel> newsFeedList, NewsAdapterClickListener adapterClickListener, Resources resources) {
        this.mCtx = mCtx;
        this.articleList = newsFeedList;
        this.adapterClickListener = adapterClickListener;
        this.resources = resources;
    }

    @Override
    public ImageAdapter.NewsFeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.item_image, parent, false);
        return new ImageAdapter.NewsFeedViewHolder(view);
    }

    @SuppressLint({"ResourceAsColor", "RestrictedApi"})
    @Override
    public void onBindViewHolder(ImageAdapter.NewsFeedViewHolder holder, int position) {
        WeatherModel article = articleList.get(position);
        //
        Glide.with(mCtx).load(article.getImgBitmap()).placeholder(resources.getDrawable(R.drawable.placeholder)).into(holder.articleImageView);


    }

    public void UpdateImageList(List<WeatherModel> weatherModelList) {
        this.articleList = weatherModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class NewsFeedViewHolder extends RecyclerView.ViewHolder {

        ImageView articleImageView;

        public NewsFeedViewHolder(View itemView) {
            super(itemView);
            articleImageView = itemView.findViewById(R.id.camera_image);
            articleImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapterClickListener.onArticleItemClicked(articleList.get(getAdapterPosition()));
                }
            });

        }


    }


}
