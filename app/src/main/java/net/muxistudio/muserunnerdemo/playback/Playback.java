package net.muxistudio.muserunnerdemo.playback;

public interface Playback {

    //control MusicPlayback to PlayMusic(BGM)
    void start();
    void stop();
    void setState();
    void getState();
    boolean isConnected();
    boolean isPlaying();
    long getCurrentStreamPosition();
    void updateLastKnownSteamPosition();
    void play();

}
