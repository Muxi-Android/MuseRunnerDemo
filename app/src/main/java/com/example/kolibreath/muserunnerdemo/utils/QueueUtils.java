package com.example.kolibreath.muserunnerdemo.utils;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.example.kolibreath.muserunnerdemo.presenter.IMusicSourcePresenter;

import java.util.ArrayList;
import java.util.List;

public class QueueUtils {

    private static List<MediaSessionCompat.QueueItem> convertToQueue(
            Iterable<MediaMetadataCompat>tracks, String... categories
    ){
        List<MediaSessionCompat.QueueItem> queue = new ArrayList<>();
        //play count/order
        int count = 0;
        for(MediaMetadataCompat metadata: tracks){
            //mediaMetaIDs are set during the network request
            String hirearchyAwareMediaID = MediaIDUtils.createMediaID(
                    metadata.getDescription().getMediaId(),categories
            );

            MediaMetadataCompat temp = new MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID,hirearchyAwareMediaID)
                    .build();

            MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(
                    metadata.getDescription(),count ++);

            queue.add(item);
        }
        return queue;
    }

    //todo without "MusicProvider"
    public static List<MediaSessionCompat.QueueItem> getPlayingItem(String mediaID, IMusicSourcePresenter presenter){
        String[] hierarchy = MediaIDUtils.getHierarchy(mediaID);

        if(hierarchy.length !=  2) return  null;
        //value
        String categoryType = hierarchy[0], categoryValue = hierarchy[1];
        Iterable<MediaMetadataCompat> tracks = null;
        if(categoryType.equals(MediaIDUtils.MEDIA_ID_MUSICS_BY_GENRE)){
//            tracks = presenter.
        }

        return convertToQueue(tracks,hierarchy[0],hierarchy[1]);
    }

    //the mediaId can be a long value
    public static int getMusicIndex(Iterable<MediaSessionCompat.QueueItem>queueItems,
                                    long queueId){
        for(MediaSessionCompat.QueueItem item: queueItems){
            int index = 0;
            if(queueId == item.getQueueId()){
                return index;
            }
            index++;
        }
        return -1;
    }

    //the mediaId can be a String value
    public static int getMusicIndex(Iterable<MediaSessionCompat.QueueItem> queueItems,
                                    String queueId){
        for(MediaSessionCompat.QueueItem item : queueItems ){
            int index = 0;
            if(queueId.equals(item.getQueueId())){
                return index;
            }
            index++;
        }
        return -1;

    }

    //as long as the index is in the range of queue while the queue exists
    public static boolean isIndexPlayable(int index, List<MediaSessionCompat.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }
}
