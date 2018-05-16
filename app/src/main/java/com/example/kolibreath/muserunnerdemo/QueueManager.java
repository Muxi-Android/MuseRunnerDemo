package com.example.kolibreath.muserunnerdemo;

import android.content.res.Resources;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.kolibreath.muserunnerdemo.presenter.IMusicSourcePresenter;

import java.util.List;

public class QueueManager {

    public QueueManager(IMusicSourcePresenter presenter,
                        Resources resources,){

    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);
        void onMetadataRetrieveError();
        void onCurrentQueueIndexUpdated(int queueIndex);
        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
