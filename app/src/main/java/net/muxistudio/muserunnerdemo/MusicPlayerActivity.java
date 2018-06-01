package net.muxistudio.muserunnerdemo;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.TextUtils;
import android.util.Log;

import net.muxistudio.muserunnerdemo.presenter.IMusicPlayControlPresenter;
import net.muxistudio.muserunnerdemo.presenter.IMusicPlaycontrolPresenterImpl;
import net.muxistudio.muserunnerdemo.view.IBrowseView;


public class MusicPlayerActivity extends BaseActivity implements IBrowseView {


    public static final String EXTRA_START_FULLSCREEN = "net.muxistudio.ui.MusicPlayerActivity.start_full_screen";
    public static final String EXTRA_START_CURRENT_DESCRIPTION = "net.muxistudio.ui.MusicPlayerActivity.current_mediaDescription";
    private final static String TAG="MusicPlayActivity";
    private final static String FRAGMENT_TAG="media_list_item";
    private IMusicPlayControlPresenter mIMusicPlay;
    private MediaBrowserCompat mMediaBrowser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        //有点奇怪...
        mIMusicPlay =new IMusicPlaycontrolPresenterImpl(this,this,this);
        mMediaBrowser=mIMusicPlay.getMediaBrowser();
    }

    @Override
    public void onStart(){
        super.onStart();
        mMediaBrowser.connect();
    }

    @Override
    public void onStop(){
        super.onStop();
        mMediaBrowser.disconnect();
        mIMusicPlay.onDistory();
    }

    public void onMediaItemSelect(MediaBrowserCompat.MediaItem mediaItem){
        if(mediaItem.isPlayable()){
            MediaControllerCompat.getMediaController(MusicPlayerActivity.this)
                    .getTransportControls().playFromMediaId(mediaItem.getMediaId(),null);
        }else if (mediaItem.isBrowsable())
            navigateToBrowser(mediaItem.getMediaId());
        else
            Log.d(TAG, "onMediaItemSelect: not find");

    }

    public void navigateToBrowser(String mediaId){
        MediaBrowerFragment fragment=getBrowseFragment();
        if (fragment==null|| TextUtils.equals(fragment.getMediaId(),mediaId)){
            fragment=new MediaBrowerFragment();
            fragment.setMediaId(mediaId);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(
                    R.animator.slide_in_from_right, R.animator.slide_out_to_left,
                    R.animator.slide_in_from_left, R.animator.slide_out_to_right);
            transaction.replace(R.id.container, fragment, FRAGMENT_TAG);
            if (mediaId != null) {
                transaction.addToBackStack(null);
            }
            transaction.commit();

        }
    }

    private MediaBrowerFragment getBrowseFragment() {
        return (MediaBrowerFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
    }

    public IMusicPlayControlPresenter getIMusicPlay(){
        return mIMusicPlay;
    }

    @Override
    public void updataData  () {
        getBrowseFragment().getAdapter().notifyDataSetChanged();
    }
}
