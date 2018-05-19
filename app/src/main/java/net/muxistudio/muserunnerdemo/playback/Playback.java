package net.muxistudio.muserunnerdemo.playback;

import android.support.v4.media.session.MediaSessionCompat;

public interface Playback {

    //control MusicPlayback to PlayMusic(BGM)
    void start();

    void stop();

    void setState();

    int getState();

    boolean isConnected();

    boolean isPlaying();

    long getCurrentStreamPosition();

    void updateLastKnownSteamPosition();

    void play(MediaSessionCompat.QueueItem queueItem);

    void pause();

    void seekTo(long position);

    void setCurrentMediaId(String mediaId);

    String getCurrentMediaId();

    interface Callback{
        void onCompletion();

        void onPlaybackStatusChanged(int state);

        void onError(String msg);
    }

    void setCallback(Callback callback);
}
