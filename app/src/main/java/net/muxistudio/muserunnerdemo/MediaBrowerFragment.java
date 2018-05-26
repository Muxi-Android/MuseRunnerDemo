package net.muxistudio.muserunnerdemo;

import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import net.muxistudio.muserunnerdemo.R;

import net.muxistudio.muserunnerdemo.Adapter.MediaItemAdapter;

/**
 * Created by MessiLP-wpy on 18-5-20.
 */

public class MediaBrowerFragment extends Fragment {
    private static final String MEDIA_TAG="media_id_tag";
    private MediaItemAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View rootView=inflater.inflate(R.layout.fragment_list_view,container,false);
        mAdapter=new MediaItemAdapter(getActivity());
        ListView listView=rootView.findViewById(R.id.list_view);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                MediaBrowserCompat.MediaItem item=mAdapter.getItem(i);
                MusicPlayerActivity musicPlayerActivity=(MusicPlayerActivity)getActivity();
                musicPlayerActivity.onMediaItemSelect(item);
            }
        });
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }


    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    public String getMediaId(){
        Bundle arg=getArguments();
        if (arg!=null)
        return getArguments().getString(MEDIA_TAG);
        else
            return null;
    }

    public void setMediaId(String mediaId){
        Bundle bundle=new Bundle();
        bundle.putString(MEDIA_TAG,mediaId);
        setArguments(bundle);
    }

    public MediaItemAdapter getAdapter(){
        return mAdapter;
    }


}
