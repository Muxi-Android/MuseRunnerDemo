package net.muxistudio.muserunnerdemo.presenter;

import android.content.Context;
import android.support.v4.media.MediaBrowserCompat;

import com.example.kolibreath.muserunnerdemo.view.IBrowseView;
import com.example.kolibreath.muserunnerdemo.view.IControlView;

public class IMusicPlaycontrolPresenterImpl implements IMusicPlayControlPresenter {

    private Context mContext;
    private IControlView mIControlView;
    private IBrowseView mIBrowseView;

    @Override
    public MediaBrowserCompat getMediaBrowser() {
        return null;
    }

    @Override
    public void play() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void next() {

    }

    @Override
    public void previous() {

    }

    public IMusicPlaycontrolPresenterImpl(Context context, IBrowseView iBrowseView,
                                          IControlView iControlView){
        this.mContext = context;
        this.mIControlView = iControlView;
        this.mIBrowseView = iBrowseView;

    }


}
