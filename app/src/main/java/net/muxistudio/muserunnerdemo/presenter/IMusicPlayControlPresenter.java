package net.muxistudio.muserunnerdemo.presenter;

import android.os.RemoteException;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaSessionCompat;

public interface IMusicPlayControlPresenter {
    MediaBrowserCompat getMediaBrowser();

    /**
     * 在该方法中创建MediaControl
     * @param token
     * @throws RemoteException
     */
    void connectToSession(MediaSessionCompat.Token token)throws RemoteException;

    /**
     * onConnect，disConnect都在activity中执行
     * 在该方法中取消activity等的引用
     */
    void onDistory();

    /**
     * 当MediaBrowser已经连接时调用
     * 这个方法可在 fragment.onStart() 或 已知Activity在onStart后完成了连接的情况下调用
     * 在这里面执行subscribe，unsubscribe操作以及getroot
     * 并且执行view的updateTitle();
     */
    void onConnected();
    void play();
    void pause();
    void next();
    void previous();



}
