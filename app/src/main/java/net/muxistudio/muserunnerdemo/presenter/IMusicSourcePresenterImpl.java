package net.muxistudio.muserunnerdemo.presenter;

import android.content.res.Resources;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import android.util.Log;


import net.muxistudio.muserunnerdemo.model.MusicMetaData;
import net.muxistudio.muserunnerdemo.model.MutableMediaMetadata;
import net.muxistudio.muserunnerdemo.utils.NetHelper;
import net.muxistudio.muserunnerdemo.view.IBrowseView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by MessiLP-wpy on 18-5-17.
 * 该类将uamp中的Gsonsource和musicProvider结合在一起
 */
public class IMusicSourcePresenterImpl implements IMusicSourcePresenter {
    private IBrowseView mBrowseView;
    //MusicMetaData是一个list
    private MusicMetaData mMusicMedaData;
    private final static String TAG="IMusicSourcePresenter";
    //音乐数据分类（按音乐类型）缓存
    private ConcurrentMap<String, List<MediaMetadataCompat>> mMusicListByGenre;
    private  ConcurrentMap<String, MutableMediaMetadata> mMusicListById;

    private Iterator<MediaMetadataCompat> mIterator;
    enum State {NON_INITIALIZED, INITIALIZING, INITIALIZED}
    //同步锁
    private volatile State mCurrentState = State.NON_INITIALIZED;

    public interface Callback {
        void onMusicCatalogReady();
    }

    public IMusicSourcePresenterImpl(){
        mMusicListByGenre = new ConcurrentHashMap<>();
        mMusicListById = new ConcurrentHashMap<>();

    }

    //我
    // 简单的先写了一点 为了自己的需要...
    //这个构造方法...有点迷
    public IMusicSourcePresenterImpl(IBrowseView view){
        this.mBrowseView = view;
    }


    /**
     * Get an iterator over the list of genres
     *
     * @return genres
     */
    public Iterable<String> getGenres() {
        if (mCurrentState != State.INITIALIZED) {
            return Collections.emptyList();
        }
        return mMusicListByGenre.keySet();
    }


    @Override
    public Iterator<MediaMetadataCompat> iterator() {
        return mIterator ;
    }

    /**
     * Get music tracks of the given genre
     *
     */
    public List<MediaMetadataCompat> getMusicsByGenre(String genre) {
        if (mCurrentState != State.INITIALIZED || !mMusicListByGenre.containsKey(genre)) {
            return Collections.emptyList();
        }
        return mMusicListByGenre.get(genre);
    }

    @Override
    public MediaMetadataCompat getMusic(String musicId) {
        return mMusicListById.containsKey(musicId) ? mMusicListById.get(musicId).metadata : null;
    }

    public boolean isInitialized() {
        return mCurrentState == State.INITIALIZED;
    }



    @Override
    public void execRequest(final Callback callback) {
        final ArrayList<MediaMetadataCompat> tracks= new ArrayList<>();
        NetHelper.getNetHelper().getApi()
                .getMusic()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MusicMetaData>() {
                    @Override
                    public void onCompleted() {
                        callback.onMusicCatalogReady();
                        Log.d(TAG, "onCompleted:getMusic Success ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        mCurrentState=State.NON_INITIALIZED;
                        Log.d(TAG, "onError: get music fail");
                    }

                    @Override
                    public void onNext(MusicMetaData musicMetaData) {
                        List<MusicMetaData.MusicBean>list=musicMetaData.getMusic();
                            for (int i=0;i<list.size();i++){
                                tracks.add(buildFromJSON(list.get(i)));
                            }

                            mIterator = tracks.iterator();

                            if (mCurrentState==State.NON_INITIALIZED){
                                mCurrentState=State.INITIALIZED;
                                    Iterator<MediaMetadataCompat> iterator=tracks.iterator();
                                    if (iterator.hasNext()){
                                        MediaMetadataCompat item = iterator.next();
                                        String musicId = item.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID);
                                        mMusicListById.put(musicId, new MutableMediaMetadata(musicId, item));
                                    }

                                }
                                buildListsByGenre();
                            }
                });

    }

    //该方法还没写完...
    @Override
    public List<MediaBrowserCompat.MediaItem> getChildren(String mediaId, Resources resources) {
        return null;
    }

    private synchronized void buildListsByGenre() {
        ConcurrentMap<String, List<MediaMetadataCompat>> newMusicListByGenre = new ConcurrentHashMap<>();

        for (MutableMediaMetadata m : mMusicListById.values()) {
            String genre = m.metadata.getString(MediaMetadataCompat.METADATA_KEY_GENRE);
            List<MediaMetadataCompat> list = newMusicListByGenre.get(genre);
            if (list == null) {
                list = new ArrayList<>();
                newMusicListByGenre.put(genre, list);
            }
            list.add(m.metadata);
        }
        mMusicListByGenre = newMusicListByGenre;
    }

    //getChildren

    private MediaMetadataCompat buildFromJSON(MusicMetaData.MusicBean musicBean) {
        String title = musicBean.getTitle();
        String album = musicBean.getAlbum();
        String artist = musicBean.getArtist();
        String genre = musicBean.getGenre();
        String source = musicBean.getSource();
        String iconUrl = musicBean.getImage();
        int trackNumber = musicBean.getTrackNumber();
        int totalTrackCount = musicBean.getTotalTrackCount();
        int duration = musicBean.getDuration() * 1000; // ms

        // Media is stored relative to JSON file
        source=buildSource(source);
        iconUrl=buildSource(iconUrl);
        // Since we don't have a unique ID in the server, we fake one using the hashcode of
        // the music source. In a real world app, this could come from the server.
        String id = String.valueOf(source.hashCode());

        // Adding the music source to the MediaMetadata (and consequently using it in the
        // mediaSession.setMetadata) is not a good idea for a real world music app, because
        // the session metadata can be accessed by notification listeners. This is done in this
        // sample for convenience only.
        //noinspection ResourceType
        // 在现实的music app中往MediaMetadata添加music source（因此在mediaSession.setMetadata中使用）并非是一个好主意
        // 因为session metadata可以被notification监听者访问
        // 为了方便起见，请按本示例进行操作
        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id)
                .putString(IMusicSourcePresenter.CUSTOM_METADATA_TRACK_SOURCE, source)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
                .putString(MediaMetadataCompat.METADATA_KEY_GENRE, genre)
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, iconUrl)
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, title)
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, trackNumber)
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, totalTrackCount)
                .build();
    }

    public String buildSource(String source){
        String basePath="http://storage.googleapis.com/automotive-media/";
        if (!source.startsWith("http")) {
            //最终的地址为http://storage.googleapis.com/automotive-media/ + source(如json中的"source" : "Jazz_In_Paris.mp3",)
            source = basePath + source;
        }
        return source;
    }


}
