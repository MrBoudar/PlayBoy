package com.example.mrboudar.playboy.api;

import org.jsoup.nodes.Element;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by MrBoudar on 16/8/13.
 */
public class HttpMethods {
    private static final String VIDEO_API_PATH = "http://106.75.11.245:10087";

    private final int TIME_OUT = 2;

    private Retrofit retrofit;

    private ApiService apiService;


    private HttpMethods() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        retrofit = new Retrofit.Builder().baseUrl(VIDEO_API_PATH).addConverterFactory(GsonConverterFactory.create()).addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(httpClient.build()).build();
        apiService = retrofit.create(ApiService.class);
    }

    private static class SingletonHolder{
        private static final HttpMethods INSTANCE = new HttpMethods();
    }
    public static HttpMethods getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public ApiService getApiService(){
        return apiService;
    }
}
