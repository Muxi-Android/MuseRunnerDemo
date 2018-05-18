package net.muxistudio.muserunnerdemo.utils;


import net.muxistudio.muserunnerdemo.model.MusicMetaData;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by MessiLP-wpy on 18-5-17.
 * retrofit+rxjava的单例模式
 */

public class NetHelper {
    private YoutubeApi mYoutubeApi;
    private static NetHelper mSingleInstance;

    public YoutubeApi getApi() {
        return mYoutubeApi;
    }
    private NetHelper() {
        HttpLoggingInterceptor interceptor = new
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .writeTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://storage.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mYoutubeApi=retrofit.create(YoutubeApi.class);
    }

    public static NetHelper getNetHelper(){
        if (mSingleInstance==null){
            synchronized (NetHelper.class){
                if (mSingleInstance==null){
                    mSingleInstance=new NetHelper();
                }
            }

        }
        return mSingleInstance;

    }



    public interface YoutubeApi{
        @GET("/automotive-media/music.json")
        Observable<MusicMetaData>getMusic();

    }
}
