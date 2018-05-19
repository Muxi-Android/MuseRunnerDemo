package net.muxistudio.muserunnerdemo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;


import net.muxistudio.muserunnerdemo.playback.MusicService;
import net.muxistudio.muserunnerdemo.utils.ResourceHelper;

//todo add Previous and back
public class MediaNotificationManager extends BroadcastReceiver{


    private final MusicService mMusicService;
    private MediaSessionCompat.Token mSessionToken;

    private MediaControllerCompat mController ;
    private MediaControllerCompat.TransportControls mTransportControls;
    private MediaMetadataCompat mMetaData;
    private PlaybackStateCompat mPlaybackState;

    private static final String ACTION_PLAY = "net.muxistudio.museRunner.action_play";
    private static final String ACTION_PAUSE = "net.muxistudio.museRunner.action_pause";
    private static final String ACTION_STOP = "net.muxistudio.museRunner.action_stop";
    private static final String ACTION_NEXT = "net.muxistudio.museRunner.action_next";
    private static final String ACTION_PREV = "net.muxistudio.museRunner.action_previous";
    private static final String CHANNEL_ID = "net.muxistudio.museRunner.channel_id";

    private static int mNotificationColor;

    private NotificationManager mNotificationManager;

    private static final int NOTIFICATION_ID = 888;
    private static final int REQUEST_CODE = -1;

    private PendingIntent mPauseIntent, mPlayIntent, mPreviousIntent,
    mNextIntent, mStopIntent;

    private boolean mStarted = false;

    //todo
    private final MediaControllerCompat.Callback mCb = new MediaControllerCompat.Callback() {
        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
            try {
                updateSessionToken();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat state) {
            mPlaybackState = state;
            if (state.getState() == PlaybackStateCompat.STATE_STOPPED ||
                    state.getState() == PlaybackStateCompat.STATE_NONE){
                stopNotification();
           }else {
                Notification notification = createNotification();
                if(notification !=  null){
                    mNotificationManager.notify(NOTIFICATION_ID,notification);
                }
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
           mMetaData = metadata;
           Notification notification = createNotification();
           if(notification != null)
               mNotificationManager.notify(NOTIFICATION_ID,notification);
        }
    };

    public MediaNotificationManager(MusicService musicService) throws RemoteException {
        this.mMusicService = musicService;
        //todo updateSessionToken()
        updateSessionToken();

        //get Theme color
        try {
            mNotificationColor = ResourceHelper.getThemeColor(mMusicService,R.attr.colorPrimary, Color.GRAY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mNotificationManager = (NotificationManager) musicService.getSystemService(Context.NOTIFICATION_SERVICE);

        String packageName = mMusicService.getPackageName();
        mPauseIntent = PendingIntent.getBroadcast(mMusicService,REQUEST_CODE,new Intent(ACTION_PAUSE).setPackage(packageName)
        ,PendingIntent.FLAG_CANCEL_CURRENT);
        mPlayIntent = PendingIntent.getBroadcast(mMusicService,REQUEST_CODE,new Intent(ACTION_PLAY).setPackage(packageName)
                ,PendingIntent.FLAG_CANCEL_CURRENT);
        mStopIntent = PendingIntent.getBroadcast(mMusicService,REQUEST_CODE,new Intent(ACTION_STOP).setPackage(packageName)
                ,PendingIntent.FLAG_CANCEL_CURRENT);
        mPreviousIntent = PendingIntent.getBroadcast(mMusicService,REQUEST_CODE,new Intent(ACTION_PREV).setPackage(packageName)
                ,PendingIntent.FLAG_CANCEL_CURRENT);
        mNextIntent = PendingIntent.getBroadcast(mMusicService,REQUEST_CODE,new Intent(ACTION_NEXT).setPackage(packageName)
                ,PendingIntent.FLAG_CANCEL_CURRENT);

    }

    private int addActions(final NotificationCompat.Builder notificationBuilder) {
        int playPauseButtonPosition = 0;
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS) != 0) {
            //todo using proper mipmap
//            notificationBuilder.addAction(R.drawable.ic_skip_previous_white_24dp,
//                    mMusicService.getString(R.string.label_previous), mPreviousIntent);
            playPauseButtonPosition = 1;
        }

        // Play or pause button, depending on the current state.
        final String label;
        final int icon;
        final PendingIntent intent;
        if (mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
//            label = mMusicService.getString(R.string.label_pause);
//            icon = R.drawable.uamp_ic_pause_white_24dp;
//            intent = mPauseIntent;
        } else {
//            label = mService.getString(R.string.label_play);
//            icon = R.drawable.uamp_ic_play_arrow_white_24dp;
//            intent = mPlayIntent;
        }
//        notificationBuilder.addAction(new NotificationCompat.Action(icon, label, intent));

        // If skip to next action is enabled
        if ((mPlaybackState.getActions() & PlaybackStateCompat.ACTION_SKIP_TO_NEXT) != 0) {
//            notificationBuilder.addAction(R.drawable.ic_skip_next_white_24dp,
//                    mService.getString(R.string.label_next), mNextIntent);
        }

        return playPauseButtonPosition;
    }

    private void setNotificationPlaybackState(NotificationCompat.Builder builder){
        if (mPlaybackState == null || !mStarted){
            mMusicService.stopForeground(true);
        }

        builder.setOngoing(mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING);
    }

    private PendingIntent createContentPIntent(MediaDescriptionCompat description){
        Intent openUI = new Intent(mMusicService,MusicPlayerActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        openUI.putExtra(MusicPlayerActivity.EXTRA_START_FULLSCREEN,true);

        if(description != null){
            openUI.putExtra(MusicPlayerActivity.EXTRA_START_CURRENT_DESCRIPTION,description);
        }

        return PendingIntent.getActivity(mMusicService,REQUEST_CODE,openUI,PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private Notification createNotification(){
        if(mMetaData != null || mPlaybackState == null){
            return null;
        }

        MediaDescriptionCompat description = mMetaData.getDescription();

        String fetchArtUrl = null;
        Bitmap art = null;

        if(description.getIconUri() != null){
            String artUrl = description.getIconUri().toString();

            //todo 从缓存中读取图片
        }

        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(mMusicService,
                CHANNEL_ID);

        notificationBuilder.setStyle(new android.support.v4.media.app
                .NotificationCompat.MediaStyle()
                .setShowActionsInCompactView(0,1,2)
                .setShowCancelButton(true)
                .setCancelButtonIntent(mStopIntent)
                .setMediaSession(mSessionToken))
                //删除这个deleteIntent
                .setDeleteIntent(mStopIntent)
                .setColor(mNotificationColor)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .setContentIntent(createContentPIntent(description))
                .setContentTitle(description.getTitle())
                .setContentText(description.getSubtitle())
                .setLargeIcon(art);

        setNotificationPlaybackState(notificationBuilder);

        //todo set the image bitmap and stash it
        //todo at last notify the notification

        return notificationBuilder.build();
    }

    public void startNotification(){
        if(!mStarted){
            mMetaData = mController.getMetadata();
            mPlaybackState = mController.getPlaybackState();

            Notification notification = createNotification();
            if(notification !=  null){
                mController.registerCallback(mCb);
                IntentFilter filter = new IntentFilter();
                filter.addAction(ACTION_NEXT);
                filter.addAction(ACTION_PAUSE);
                filter.addAction(ACTION_PLAY);
                filter.addAction(ACTION_PREV);

                mMusicService.startForeground(NOTIFICATION_ID,notification);

                mStarted = true;
            }
        }
    }

    public void stopNotification(){
        if(mStarted){
            mStarted = false;
            mController.unregisterCallback(mCb);

            mNotificationManager.cancel(NOTIFICATION_ID);
            mMusicService.unregisterReceiver(this);

            mMusicService.stopForeground(true);
        }
    }

    private void updateSessionToken() throws RemoteException {
        MediaSessionCompat.Token freshToken = mMusicService.getSessionToken();
        if(mSessionToken ==null && freshToken!=null || mSessionToken !=null && !mSessionToken.equals(freshToken)){
            if(mController != null){
                mController.unregisterCallback(mCb);
            }
            mSessionToken = freshToken;
            if(mSessionToken !=null ){
                mController = new MediaControllerCompat(mMusicService,mSessionToken);
                mTransportControls = mController.getTransportControls();

                if(mStarted){
                    mController.registerCallback(mCb);
                }
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action){
            case ACTION_PLAY:
                mTransportControls.play();
                break;
            case ACTION_PAUSE:
                mTransportControls.pause();
                break;
            case ACTION_NEXT:
                mTransportControls.skipToNext();
                break;
            case ACTION_PREV:
                mTransportControls.skipToPrevious();
                break;
        }
    }
}
