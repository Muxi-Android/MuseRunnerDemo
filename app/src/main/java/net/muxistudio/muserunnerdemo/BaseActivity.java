package net.muxistudio.muserunnerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import net.muxistudio.muserunnerdemo.view.IControlView;

/**
 * Created by MessiLP-wpy on 18-5-20.
 * 只是分担了controlview
 */

public abstract class BaseActivity extends AppCompatActivity implements IControlView {

    private MediaControlFragment mMediaCotrolFragment;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart(){
        super.onStart();
        mMediaCotrolFragment=(MediaControlFragment)getSupportFragmentManager()
                .findFragmentById(R.id.fragment_playback_controls);
        if (mMediaCotrolFragment==null)
            throw new IllegalStateException("Mising fragment with id 'controls'. Cannot continue.");
        hidePlaybackControl();

    }

    @Override
    public void hidePlaybackControl(){
        getSupportFragmentManager().beginTransaction()
                .hide(mMediaCotrolFragment)
                .commit();
    }

    @Override
    public void showPlaybackControl(){
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(
                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom,
                            R.animator.slide_in_from_bottom, R.animator.slide_out_to_bottom)
                    .show(mMediaCotrolFragment)
                    .commit();

    }
}

