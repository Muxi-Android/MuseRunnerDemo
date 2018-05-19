package net.muxistudio.muserunnerdemo.playback;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import net.muxistudio.muserunnerdemo.presenter.IMusicSourcePresenter;
import net.muxistudio.muserunnerdemo.utils.MediaIDUtils;

import static com.google.android.exoplayer2.C.CONTENT_TYPE_MUSIC;
import static com.google.android.exoplayer2.C.USAGE_MEDIA;

//import android.media.AudioAttributes;

// the management of audio focus and beat playing
//this class will implements the Playback
//the MusicPlaybackManager will implements the Playback.Callback
public class MusicPlayback implements  Playback{

    private String TAG = "net.muxistudio.museRunner.MusicPlayback";
    private boolean mPlayOnFocusGain;
    private SimpleExoPlayer mPlayer;

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

    private Callback mCallback;
    private ExoPlayerEventListener mEventListener = new ExoPlayerEventListener();

    private String mCurrentMediaId = "";
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
            mPlayer.removeListener(mEventListener);
            mPlayer = null;
            mExoPlayerNullorStopped = true;
            mPlayOnFocusGain  = false;
        }

        if(mWifiLock.isHeld()){
            mWifiLock.release();
        }
    }

    private void releaseAudioFocus(){
        if(mAudioManager.abandonAudioFocus(mAudioChangeListener)
                == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;
        }
    }

    private void retrieveAudioFocus(){
        int result = mAudioManager.requestAudioFocus(
                mAudioChangeListener,
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if(result ==  AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
            mCurrentAudioFocusState = AUDIO_FOCUSED;
        else
            mCurrentAudioFocusState = AUDIO_NO_FOCUS_NO_DUCK;

    }

    //todo to fullfill
    @Override
    public void start() {}

    @Override
    public void stop() {
        releaseAudioFocus();
        unregisterNoisyReceiver();
        releaseResources(true);
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
            case Player.STATE_IDLE:
                return PlaybackStateCompat.STATE_PAUSED;
            case Player.STATE_BUFFERING:
                return PlaybackStateCompat.STATE_BUFFERING;
            case Player.STATE_READY:
                return mPlayer.getPlayWhenReady()
                        ? PlaybackStateCompat.STATE_PLAYING
                        : PlaybackStateCompat.STATE_PAUSED;
            case Player.STATE_ENDED:
                return PlaybackStateCompat.STATE_PAUSED;
           default:
                return PlaybackStateCompat.STATE_NONE;
        }
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public boolean isPlaying() {
        return mPlayOnFocusGain || (mPlayer != null && mPlayer.getPlayWhenReady());
    }

    @Override
    public long getCurrentStreamPosition() {
        return
                mPlayer !=  null ? mPlayer.getCurrentPosition() :0 ;
    }

    @Override
    public void updateLastKnownSteamPosition() {

    }


    private MediaSource getMediaSource(String source){
        DataSource.Factory dataSourceFactory= new DefaultDataSourceFactory(mContext,
                TAG);
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(source),dataSourceFactory,extractorsFactory,null,null);

        return mediaSource;
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void play(MediaSession.QueueItem item) {
        mPlayOnFocusGain = true;
        retrieveAudioFocus();
        registerNoisyReceiver();
        String mediaId = item.getDescription().getMediaId();
        boolean mediaChange  = !TextUtils.equals(mediaId,getCurrentMediaId());

        if(mediaChange){
            mCurrentMediaId = mediaId;
        }

        if(mediaChange || mPlayer == null){
            //release everything without the player
            releaseResources(false);
            MediaMetadataCompat track= mMSPresenter.getMusic(
                    MediaIDUtils.extractMusicIDFromMediaID(item.getDescription().getMediaId()));
           String sourceUri = track.getString(IMusicSourcePresenter.CUSTOM_METADATA_TRACK_SOURCE);

           if(sourceUri != null){
               sourceUri = sourceUri.replaceAll(" ","%20");
           }


           if(mPlayer == null){
               mPlayer = ExoPlayerFactory.newSimpleInstance(
                       new DefaultRenderersFactory(mContext),new DefaultTrackSelector(),new DefaultLoadControl());

               mPlayer.addListener(mEventListener);
           }

           MediaSource mediaSource = getMediaSource(sourceUri);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType(CONTENT_TYPE_MUSIC)
                    .setUsage(USAGE_MEDIA)
                    .build();

            mPlayer.setAudioAttributes(audioAttributes);

            mPlayer.prepare(mediaSource);
            mWifiLock.acquire();
        }

        configurePlayerState();
    }

    @Override
    public void pause() {
        if(mPlayer != null){
            mPlayer.setPlayWhenReady(false);
        }
        releaseResources(false) ;
        unregisterNoisyReceiver();
    }

    @Override
    public void seekTo(long position) {
        if(mPlayer != null){
            registerNoisyReceiver();
            mPlayer.seekTo(position);
        }
    }

    @Override
    public void setCurrentMediaId(String mediaId) {
        this.mCurrentMediaId = mediaId;
    }

    @Override
    public String getCurrentMediaId() {
        return mCurrentMediaId;
    }

    @Override
    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    private void configurePlayerState(){
          if(mCurrentAudioFocusState ==  AUDIO_NO_FOCUS_NO_DUCK){
              pause();
          }else{
              registerNoisyReceiver();
              if(mCurrentAudioFocusState == AUDIO_NO_FOCUS_CAN_DUCK){
                  mPlayer.setVolume(VOLUME_DUCK);
              } else{
                  mPlayer.setVolume(VOLUME_NORMAL);
              }
          }

          if(mPlayOnFocusGain){
              mPlayer.setPlayWhenReady(true) ;
              mPlayOnFocusGain = false;
          }
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


    //todo exoplayer example
    private final class ExoPlayerEventListener implements  Player.EventListener{

        //called when the timeline changed
        //todo to fullfill
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest) {
            //none
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            //none
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            //none
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState){
                case Player.STATE_IDLE:
                    break;
                case Player.STATE_BUFFERING:
                    break;
                    //the listener is ready the playback will proceed   
                case Player.STATE_READY:
                    if(mCallback != null){
                        mCallback.onPlaybackStatusChanged(getState());
                    }
                    break;
                case Player.STATE_ENDED:
                    if( mCallback !=  null){
                        mCallback.onCompletion();
                    }
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            //none
        }

        //todo have a more effective way to send out message
        @Override
        public void onPlayerError(ExoPlaybackException error) {
            String what;
            switch (error.type){

                 case ExoPlaybackException.TYPE_SOURCE:
                    what = error.getSourceException().getMessage();
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    what = error.getSourceException().getMessage();
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    what = error.getSourceException().getMessage();
                    break;
                default :
                    what = "Unknown " + error;
            }
            if(mCallback != null)
                mCallback.onError(" Exoplayer error" + what);

        }

        @Override
        public void onPositionDiscontinuity() {
            //none
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            //none
        }
    }
}
