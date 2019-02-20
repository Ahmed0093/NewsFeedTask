package com.development.task.newsfeedtask.network;

import com.development.task.newsfeedtask.model.NewsFeed;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NewsFeedService {
        @GET("v1/articles?source=the-next-web&apiKey=533af958594143758318137469b41ba9")
        Observable<NewsFeed> getNewsFeed();
}
