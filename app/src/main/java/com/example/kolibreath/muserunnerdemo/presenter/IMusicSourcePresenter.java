package com.example.kolibreath.muserunnerdemo.presenter;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

public interface IMusicSourcePresenter {

    //可能不知这一个函数
    void execRequest();

    Iterator<MediaMetadataCompat> iterator();
}