package net.muxistudio.muserunnerdemo.playback;

import android.os.Bundle;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import net.muxistudio.muserunnerdemo.QueueManager;
import net.muxistudio.muserunnerdemo.presenter.IMusicSourcePresenter;

public class PlaybackManager implements Playback.Callback {

    private PlaybackServiceCallback mServiceCallback;
    private IMusicSourcePresenter mPresenter;
    private QueueManager mQueueManager;
    private Playback mPlayback;
    private MediaSessionCallback
    public PlaybackManager(PlaybackServiceCallback callback,
                           IMusicSourcePresenter presenter,
                           QueueManager queueManager,
                           Playback playback){
        this.mServiceCallback = callback;
        this.mQueueManager = queueManager;
        this.mPlayback = playback;
        this.mPresenter = presenter;

    }

    public Playback getPlayback(){
        return mPlayback;
    }

    public MediaSessionCompat.Callback getMediaSessionCallback(){
        return
    }

    @Override
    public void onCompletion() {

    }

    @Override
    public void onPlaybackStatusChanged(int state) {

    }

    @Override
    public void onError(String msg) {

    }

    private void handlePlayRequest(){
        MediaSessionCompat.QueueItem currentMusic = mQueueManager.getCurrentQueueItem();
        if(currentMusic != null){
            mServiceCallback.onPlaybackStart();;
            mPlayback.play(currentMusic);
        }
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback{
        @Override
        public void onPlay() {
            if(mQueueManager.getCurrentQueueItem() == null){
                mQueueManager.setRandomQueue(mPresenter);
            }
        }

        @Override
        public void onPlayFromMediaId(String mediaId, Bundle extras) {
            super.onPlayFromMediaId(mediaId, extras);
        }

        @Override
        public void onSkipToQueueItem(long id) {
            super.onSkipToQueueItem(id);
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
        }
    }
    public interface PlaybackServiceCallback{
        void  onPlaybackStart();

        void onNotificationRequired();

        void onPlaybackStateUpdated(PlaybackStateCompat newState);
    }
}
