package net.muxistudio.muserunnerdemo.utils;


import android.graphics.Bitmap;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.net.URL;

public class WebViewUtils {

    public static void initSetting(WebView webView, String url){
        webView.loadUrl(url);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                super.onPageFinished(view,url);
            }

            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view,url,favicon);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){

        });
    }
}
