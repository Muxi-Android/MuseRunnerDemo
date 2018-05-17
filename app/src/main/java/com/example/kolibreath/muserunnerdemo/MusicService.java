package com.example.kolibreath.muserunnerdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaBrowserServiceCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

public class MusicService extends MediaBrowserServiceCompat {

    private QueueManager.MetadataUpdateListener mMetadataUpdateListenter;

    public MusicService() {
        super();
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
