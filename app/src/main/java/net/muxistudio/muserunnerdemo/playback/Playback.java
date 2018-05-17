package net.muxistudio.muserunnerdemo.playback;

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

    void play();

    void pause();

    void seekTo();

    void setCurrentMediaId(String mediaId);

    String getCurrentMediaId();

    interface Callback{
        void onCompletion();

        void onPlaybackStatusChanged(int state);

        void onError(String msg);
    }

    void setCallback(Callback callback);
}
