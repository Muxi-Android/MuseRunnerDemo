package net.muxistudio.muserunnerdemo;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;

import net.muxistudio.muserunnerdemo.presenter.IMusicPlayControlPresenter;
import net.muxistudio.muserunnerdemo.presenter.IMusicPlaycontrolPresenterImpl;
import net.muxistudio.muserunnerdemo.view.IBrowseView;


public class MusicPlayerActivity extends BaseActivity implements IBrowseView {


    public static final String EXTRA_START_FULLSCREEN = "net.muxistudio.ui.MusicPlayerActivity.start_full_screen";
    public static final String EXTRA_START_CURRENT_DESCRIPTION = "net.muxistudio.ui.MusicPlayerActivity.current_mediaDescription";
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



}
