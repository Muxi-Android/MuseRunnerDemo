package com.example.kolibreath.muserunnerdemo;

import android.app.Application;
import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

/**
 * Created by kolibreath on 17-7-23.
 */

public class App extends Application {

    private HttpProxyCacheServer proxy;

    private static Context mContext;


    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        super.onCreate();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        return app.proxy == null ? (app.proxy = app.newProxy()) : app.proxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer.Builder(this)
                .maxCacheSize(1024*1024*512)
                //0.5G
                .build();
    }

    public static Context getContext() {
        if(mContext == null)mContext =getContext();
        return mContext;
    }

    /*
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            HttpProxyCacheServer proxy = getProxy();
            String proxyUrl = proxy.getProxyUrl(VIDEO_URL);
            videoView.setVideoPath(proxyUrl);
        }

        private HttpProxyCacheServer getProxy() {
            return App.getProxy(getApplicationContext());
        }
        使用的之后直接调用proxy然后使用getProxyUrl得到代理Url，用就好了～～
     */

}
