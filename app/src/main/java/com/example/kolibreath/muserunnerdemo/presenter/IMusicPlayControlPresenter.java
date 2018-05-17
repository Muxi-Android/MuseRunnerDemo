package com.example.kolibreath.muserunnerdemo.presenter;

import android.support.v4.media.MediaBrowserCompat;

public interface IMusicPlayPresenter {
    MediaBrowserCompat getMediaBrowser();

    void play();
    void pause();
    void next();
    void previous();


}
