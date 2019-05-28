package com.development.task.newsfeedtask.network;

import com.development.task.newsfeedtask.model.NewsFeed;
import com.development.task.newsfeedtask.model.WeatherResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface NewsFeedService {
//        @GET("v1/articles?source=the-next-web&apiKey=533af958594143758318137469b41ba9")
//        Observable<NewsFeed> getNewsFeed();
        @GET("forecast?q=Cairo,DE&appid=b6907d289e10d714a6e88b30761fae22")
        Observable<WeatherResponse> getCairoWeaterResponse();

}
