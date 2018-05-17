package net.muxistudio.muserunnerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class MusicPlayerActivity extends AppCompatActivity {


    public static final String EXTRA_START_FULLSCREEN = "net.muxistudio.ui.MusicPlayerActivity.start_full_screen";
    public static final String EXTRA_START_CURRENT_DESCRIPTION = "net.muxistudio.ui.MusicPlayerActivity.current_mediaDescription";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
    }
}
