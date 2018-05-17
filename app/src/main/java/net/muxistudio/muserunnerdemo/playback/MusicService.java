package net.muxistudio.muserunnerdemo.playback;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import net.muxistudio.muserunnerdemo.QueueManager;
import net.muxistudio.muserunnerdemo.presenter.IMusicSourcePresenter;
import net.muxistudio.muserunnerdemo.presenter.IMusicSourcePresenterImpl;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    private QueueManager.MetadataUpdateListener mMetadataUpdateListenter;

    //indicate a coming action
    private static final String ACTION_CMD = "net.muxistudio.muserunner.action_cmd";
    private static final String CMD_PAUSE = "net.muxistudio.muserunner.action_pause";
    private static final String CMD_NAME = "net.muxistudio.muserunner.action_name";

    private IMusicSourcePresenter msourcePresenter;
    private QueueManager mQueueManager;
    @Override
    public void onCreate() {
        super.onCreate();

        msourcePresenter = new IMusicSourcePresenterImpl();
        QueueManager mQueueManager = new QueueManager(msourcePresenter,getResources(),mMetadataUpdateListenter);

    }

    @Nullable
    @Override
    public BrowserRoot onGetRoot(@NonNull String clientPackageName, int clientUid, @Nullable Bundle rootHints) {
        return null;
    }

    @Override
    public void onLoadChildren(@NonNull String parentId, @NonNull Result<List<MediaBrowserCompat.MediaItem>> result) {

    }

    //todo to fill
    private QueueManager.MetadataUpdateListener initListener(){
        return new QueueManager.MetadataUpdateListener() {
            @Override
            public void onMetadataChanged(MediaMetadataCompat metadata) {

            }

            @Override
            public void onMetadataRetrieveError() {

            }

            @Override
            public void onCurrentQueueIndexUpdated(int queueIndex) {

            }

            @Override
            public void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue) {

            }
        };
    }


}
