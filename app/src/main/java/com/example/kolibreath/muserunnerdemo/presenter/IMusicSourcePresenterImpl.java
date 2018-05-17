package com.example.kolibreath.muserunnerdemo.presenter;

import android.support.v4.media.MediaMetadataCompat;

import com.example.kolibreath.muserunnerdemo.model.MusicMetaData;
import com.example.kolibreath.muserunnerdemo.view.IBrowseView;

import java.util.Iterator;

public class IMusicSourcePresenterImpl implements IMusicSourcePresenter {

    private IBrowseView mBrowseView;
    //MusicMetaData是一个list
    private MusicMetaData mMusicMedaData;
    //我简单的先写了一点 为了自己的需要...
    public IMusicSourcePresenterImpl(IBrowseView view){
        this.mBrowseView = view;
    }

    @Override
    public void execRequest() {
        //在回调中拿到这个MusicMetaData

    }

    //需要将MusicMetaData转换为 类似于 RemoteJSONProvider中的形式,这样我方便拿到你封装过得
    //MediaMetaData
    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return null;
    }

    @Override
    public MediaMetadataCompat getMusic(String mediaID) {
        return null;
    }
}
