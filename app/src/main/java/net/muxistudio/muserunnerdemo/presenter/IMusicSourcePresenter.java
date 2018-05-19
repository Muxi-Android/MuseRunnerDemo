package net.muxistudio.muserunnerdemo.presenter;

import android.content.res.Resources;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import java.util.Iterator;
import java.util.List;

public interface IMusicSourcePresenter {

    /**该函数完全封装执行网络请求，
    * @param callback 这个回调在请求数据成功后并封装完毕后执行，可以在接口中的方法来sendresult
    */
    void execRequest(IMusicSourcePresenterImpl.Callback callback);

    List<MediaBrowserCompat.MediaItem> getChildren(String mediaId, Resources resources);

    MediaMetadataCompat getMusic(String musicId);

    /**
     *
     * @return 表示已加载成功，不需要在进行网络请求
     */
    boolean isInitialized();
    //获取数据的source值,如"source" : "Jazz_In_Paris.mp3",用于合成完整的url
    String CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__";


}
