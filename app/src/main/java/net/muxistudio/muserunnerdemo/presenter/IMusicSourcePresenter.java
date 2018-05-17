package net.muxistudio.muserunnerdemo.presenter;

import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;

public interface IMusicSourcePresenter {

    //可能不知这一个函数
    void execRequest();

    Iterator<MediaMetadataCompat> iterator();

    //getMusic metadata from musicListbyid
    MediaMetadataCompat getMusic(String mediaID);
}
