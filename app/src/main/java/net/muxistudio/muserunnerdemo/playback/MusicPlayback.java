package net.muxistudio.muserunnerdemo.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.wifi.WifiManager;
import android.support.v4.media.session.PlaybackStateCompat;

import com.google.android.exoplayer2.ExoPlayer;

import net.muxistudio.muserunnerdemo.presenter.IMusicSourcePresenter;

// the management of audio focus and beat playing
public class MusicPlayback implements  Playback{


    private boolean mPlayOnFocusGain;
    private ExoPlayer mPlayer;

    //about duck
    private static final float VOLUME_DUCK = 0.2f;
    private static final float VOLUME_NORMAL = 1.0f;

    //about focus and duck
    private static final int AUDIO_NO_FOCUS_NO_DUCK = 0;
    private static final int AUDIO_NO_FOCUS_CAN_DUCK = 1;
    private static final int AUDIO_FOCUSED = 2;

    //the wifi signals will be not active if the screen turn off
    private WifiManager.WifiLock mWifiLock;

    private Context mContext;
    private IMusicSourcePresenter mMSPresenter;
    private AudioManager mAudioManager;

    private int mCurrentAudioFocusState = 0;

    private boolean mExoPlayerNullorStopped = false;

    private IntentFilter mNoisyIntentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
    private boolean mIsRegistered = false;

    public MusicPlayback(Context context, IMusicSourcePresenter presenter){
        this.mContext = context.getApplicationContext();
        this.mMSPresenter = presenter;
        this.mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        WifiManager manager = (WifiManager) this.mContext.getSystemService(Context.WIFI_SERVICE);
        //this tag is only for debug and identify the lock purpose
        this.mWifiLock = manager.createWifiLock(WifiManager.WIFI_MODE_FULL,"muse_runner_lock");
    }

    //the headphone is being unplugged
    private BroadcastReceiver mNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent
            .getAction())){
                if(isPlaying()){
                    Intent i = new Intent(context,MusicService.class);
                    i.setAction(MusicService.ACTION_CMD);
                    i.putExtra(MusicService.CMD_NAME,MusicService.CMD_PAUSE);
                    mContext.startService(i);
                }
            }
        }
    };

    private void registerNoisyReceiver(){
        if(!mIsRegistered){
            mContext.registerReceiver(mNoisyReceiver,mNoisyIntentFilter);
            mIsRegistered = true;
        }
    }

    private void unregisterNoisyReceiver(){
        if(mIsRegistered){
            mContext.unregisterReceiver(mNoisyReceiver);
            mIsRegistered = false;
        }
    }

    private void releaseResources(boolean isRelease){
        if(isRelease && mPlayer != null){
            mPlayer.release();
            mPlayer.removeListener();
        }
    }

    private void releaseAudioFocus(){
        if(mAudioManager.abandonAudioFocus(m))
    }

    //todo to fullfill
    @Override
    public void start() {}

    @Override
    public void stop() {
        releaseAudioFocus();
        unregisterNoisyReceiver();
        releaseResources();
    }

    @Override
    public void setState() {

    }

    /**
     * the value is retrieved from mPlayer
     */
    @Override
    public int getState() {
        if(mPlayer == null){
            return mExoPlayerNullorStopped? PlaybackStateCompat.STATE_STOPPED :
                    PlaybackStateCompat.STATE_NONE;
        }
        switch (mPlayer.getPlaybackState()){
            case ExoPlayer.STATE_IDLE:
                return PlaybackStateCompat.STATE_PAUSED;
            case ExoPlayer.STATE_BUFFERING:
                return PlaybackStateCompat.STATE_BUFFERING;
            case ExoPlayer.STATE_READY:
                return mPlayer.getPlayWhenReady()
                        ? PlaybackStateCompat.STATE_PLAYING
                        : PlaybackStateCompat.STATE_PAUSED;
            case ExoPlayer.STATE_ENDED:
                return PlaybackStateCompat.STATE_PAUSED;
           default:
                return PlaybackStateCompat.STATE_NONE;
        }
    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public boolean isPlaying() {
        return mPlayOnFocusGain || (mPlayer != null && mPlayer.getPlayWhenReady());
    }

    @Override
    public long getCurrentStreamPosition() {
        return 0;
    }

    @Override
    public void updateLastKnownSteamPosition() {

    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void seekTo() {

    }

    @Override
    public void setCurrentMediaId(String mediaId) {

    }

    @Override
    public String getCurrentMediaId() {
        return null;
    }

    @Override
    public void setCallback(Callback callback) {

    }

    private void configurePlayerState(){

    }

    private AudioManager.OnAudioFocusChangeListener mAudioChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange){
                case AudioManager.AUDIOFOCUS_GAIN:
                    mCurrentAudioFocusState = AUDIO_FOCUSED;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    mCurrentAudioFocusState = AUDIO_NO_FOCUS_CAN_DUCK;
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
                    mPlayOnFocusGain = mPlayer != null && mPlayer.getPlayWhenReady();
                    break;
                case AudioManager.AUDIOFOCUS_LOSS:
                    mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
                    break;
            }

            if(mPlayer != null)
                configurePlayerState();
        }

    };
}
