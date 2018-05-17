package com.example.kolibreath.muserunnerdemo.presenter;


import android.content.Context;
import android.support.v4.media.MediaMetadataCompat;

import com.example.kolibreath.muserunnerdemo.view.IBrowseView;
import com.example.kolibreath.muserunnerdemo.view.IControlView;

import java.util.Iterator;

public class IMusicPresenterImpl implements IMusicSourcePresenter {

    private Context mContext;
    private IControlView mIControlView;
    private IBrowseView mIBrowseView;

    @Override
    public MediaBrowserCompat getMediaBrowser() {
        return null;
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    public IMusicPresenterImpl(Context context, IBrowseView iBrowseView,
                               IControlView iControlView){
        this.mContext = context;
        this.mIControlView = iControlView;
        this.mIBrowseView = iBrowseView;

    }

    @Override
    public void execRequest() {

    }

    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return null;
    }

}
