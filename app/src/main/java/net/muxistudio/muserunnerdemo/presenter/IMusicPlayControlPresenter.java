package net.muxistudio.muserunnerdemo.presenter;

import android.support.v4.media.MediaBrowserCompat;

public interface IMusicPlayControlPresenter {
    MediaBrowserCompat getMediaBrowser();

    void play();
    void pause();
    void next();
    void previous();


}
