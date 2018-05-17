package com.example.kolibreath.muserunnerdemo.presenter;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import com.example.kolibreath.muserunnerdemo.view.IBrowseView;
import com.example.kolibreath.muserunnerdemo.view.IControlView;

//网络请求 还有根据流派分配 id还有设置key 都在这里解决
public class IMusicPresenterImpl implements IMusicPlayPresenter {

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


}
