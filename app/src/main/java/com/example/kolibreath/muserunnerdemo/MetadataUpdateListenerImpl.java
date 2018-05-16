package com.example.kolibreath.muserunnerdemo;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.List;

public class MetadataUpdateListenerImpl implements QueueManager.MetadataUpdateListener{
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
}
