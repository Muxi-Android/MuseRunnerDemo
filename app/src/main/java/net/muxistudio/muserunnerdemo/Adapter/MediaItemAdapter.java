package net.muxistudio.muserunnerdemo.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.media.MediaBrowserCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

/**
 * Created by MessiLP-wpy on 18-5-26.
 */

public class MediaItemAdapter extends ArrayAdapter<MediaBrowserCompat.MediaItem>{

    public MediaItemAdapter(Activity context){
        super(context, R.layout.media_list_item, new ArrayList<MediaBrowserCompat.MediaItem>());
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        MediaBrowserCompat.MediaItem item = getItem(position);
        return MediaItemHolder.setupListView((Activity) getContext(), convertView, parent,
                item);
    }

}
