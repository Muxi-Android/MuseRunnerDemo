package net.muxistudio.muserunnerdemo;

import android.content.res.Resources;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import net.muxistudio.muserunnerdemo.presenter.IMusicSourcePresenter;
import net.muxistudio.muserunnerdemo.utils.MediaIDUtils;
import net.muxistudio.muserunnerdemo.utils.QueueUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class QueueManager {

    private MetadataUpdateListener mListener;
    private IMusicSourcePresenter mPresenter;
    private Resources mResources;
    private int mCurrentPlayingIndex;
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;

    public QueueManager(IMusicSourcePresenter presenter,
                        Resources resources,MetadataUpdateListener listener){
        this.mListener = listener;
        this.mPresenter = presenter;
        this.mResources = resources;

        mPlayingQueue = Collections.synchronizedList(new ArrayList<MediaSessionCompat.QueueItem>());
        mCurrentPlayingIndex = 0;
    }


    public MediaSessionCompat.QueueItem getCurrentQueueItem(){
        if(!QueueUtils.isIndexPlayable(mCurrentPlayingIndex,mPlayingQueue)){
            return null;
        }
        return mPlayingQueue.get(mCurrentPlayingIndex);
    }

    public void setCurrentIndex(int index){
        if(index >= 0 && index < mPlayingQueue.size()){
            mCurrentPlayingIndex = index;
            mListener.onCurrentQueueIndexUpdated(mCurrentPlayingIndex);
        }
    }

    /**
     * @param queueId
     * @return whether the index is valid, if the index is not valid, the
     * setCurrentIndex(int index) will not performed
     */
    public boolean setCurrentQueueItem(long queueId){
        int index = QueueUtils.getMusicIndex(mPlayingQueue,queueId);
        setCurrentIndex(index);
        return  index >= 0;
    }

    /**
     * the overloaded version of setCurrentQueueItem()
     * @param mediaId
     * @return
     */
    public boolean setCurrentQueueItem(String mediaId){
        int index = QueueUtils.getMusicIndex(mPlayingQueue,mediaId);
        setCurrentIndex(index);
        return  index >= 0;
    }

    public boolean skipQueuePosition(int amount){
        int index = mCurrentPlayingIndex + amount;
        if(index < 0)
            index = 0;
        else
            index %= mPlayingQueue.size();
        if(!QueueUtils.isIndexPlayable(index,mPlayingQueue))
            return  false;
        mCurrentPlayingIndex = index;
        return true;

    }

    public boolean isSameBrowsingCategory(String mediaId){
        String[] newBrowsingHierarchy = MediaIDUtils.getHierarchy(mediaId);
        MediaSessionCompat.QueueItem current = getCurrentQueueItem();

        if( current == null)
            return false;
        String[] currentBrowingHierarchy = MediaIDUtils.getHierarchy(current.getDescription().getMediaId());

        return Arrays.equals(newBrowsingHierarchy,currentBrowingHierarchy);
    }

    public void updateMetadata(){
        MediaSessionCompat.QueueItem currentMusic = getCurrentQueueItem();
        if(currentMusic == null){
            mListener.onMetadataRetrieveError();
            return;
        }

        String musicId = MediaIDUtils.extractMusicIDFromMediaID(currentMusic.getDescription().getMediaId());
        MediaMetadataCompat metadata = mPresenter.getMusic(musicId);

        if(metadata ==  null)
            throw new IllegalArgumentException("Invalid musicId"+ musicId);

        if(metadata.getDescription().getIconBitmap() == null && metadata.getDescription().getIconUri() != null){
            String albumUri = metadata.getDescription().getIconUri().toString();
            //todo cache the bitmap and update
        }
    }

    public interface MetadataUpdateListener {
        void onMetadataChanged(MediaMetadataCompat metadata);
        void onMetadataRetrieveError();
        void onCurrentQueueIndexUpdated(int queueIndex);
        void onQueueUpdated(String title, List<MediaSessionCompat.QueueItem> newQueue);
    }
}
