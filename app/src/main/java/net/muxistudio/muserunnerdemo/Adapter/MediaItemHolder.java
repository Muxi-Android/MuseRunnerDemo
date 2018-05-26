package net.muxistudio.muserunnerdemo.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.muxistudio.muserunnerdemo.utils.MediaIDUtils;

/**
 * Created by MessiLP-wpy on 18-5-26.
 */

public class MediaItemHolder {
    public static final int STATE_INVALID = -1;
    public static final int STATE_NONE = 0;
    public static final int STATE_PLAYABLE = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_TAG=111;
    private ImageView mImageView;
    private TextView mTitleView;
    private TextView mDescriptionView;

    private static ColorStateList sColorStatePlaying;
    private static ColorStateList sColorStateNotPlaying;

    private static void initializeColorStateLists(Context ctx) {
        sColorStateNotPlaying = ColorStateList.valueOf(ctx.getResources().getColor(
                R.color.media_item_icon_not_playing));
        sColorStatePlaying = ColorStateList.valueOf(ctx.getResources().getColor(
                R.color.media_item_icon_playing));
    }



    /**
     *
     * @param activity
     * @param convertView 这个view代表被替代消失的view，也就是转换了，这样利用可以提高效率
     * @param viewGroup
     * @param mediaItem
     * @return
     */
    static View setupListView(Activity activity, View convertView, ViewGroup viewGroup,
                        MediaBrowserCompat.MediaItem mediaItem){

        if (sColorStateNotPlaying == null || sColorStatePlaying == null) {
            initializeColorStateLists(activity);
        }

        MediaItemHolder holder;
        Integer cachedState = STATE_INVALID;
        if (convertView==null){
            convertView= LayoutInflater.from(activity)
                    .inflate(R.layoyt.media_item_view);
            holder=new MediaItemHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.play_eq);
            holder.mTitleView = (TextView) convertView.findViewById(R.id.title);
            holder.mDescriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        }else {
            holder = (MediaItemHolder) convertView.getTag();
            cachedState = (Integer) convertView.getTag(STATE_TAG);
        }
        MediaDescriptionCompat description = mediaItem.getDescription();
        holder.mTitleView.setText(description.getTitle());
        holder.mDescriptionView.setText(description.getSubtitle());

        // new state.
        int state = getMediaItemState(activity, mediaItem);
        if (cachedState == null || cachedState != state) {
            Drawable drawable = getDrawableByState(activity, state);
            if (drawable != null) {
                holder.mImageView.setImageDrawable(drawable);
                holder.mImageView.setVisibility(View.VISIBLE);
            } else {
                holder.mImageView.setVisibility(View.GONE);
            }
            convertView.setTag(R.id.tag_mediaitem_state_cache, state);
        }

        return convertView;
    }

    public static Drawable getDrawableByState(Context context, int state) {

        switch (state) {
            case STATE_PLAYABLE:
                Drawable pauseDrawable = ContextCompat.getDrawable(context,
                        R.drawable.ic_play_arrow_black_36dp);
                DrawableCompat.setTintList(pauseDrawable,);
                return pauseDrawable;
            case STATE_PLAYING:
                AnimationDrawable animation = (AnimationDrawable)
                        ContextCompat.getDrawable(context, R.drawable.ic_equalizer_white_36dp);
                DrawableCompat.setTintList(animation, sColorStatePlaying);
                animation.start();
                return animation;
            case STATE_PAUSED:
                Drawable playDrawable = ContextCompat.getDrawable(context,
                        R.drawable.ic_equalizer1_white_36dp);
                DrawableCompat.setTintList(playDrawable, sColorStatePlaying);
                return playDrawable;
            default:
                return null;
        }
    }

    public static int getMediaItemState(Activity context, MediaBrowserCompat.MediaItem mediaItem) {
        int state = STATE_NONE;
        // Set state to playable first, then override to playing or paused state if needed
        if (mediaItem.isPlayable()) {
            state = STATE_PLAYABLE;
            if (MediaIDUtils.isMediaItemPlaying(context, mediaItem)) {
                state =getStateFromController(context);
            }
        }

        return state;
    }

    public static int getStateFromController(Activity context) {
        MediaControllerCompat controller = MediaControllerCompat.getMediaController(context);
        PlaybackStateCompat pbState = controller.getPlaybackState();
        if (pbState == null ||
                pbState.getState() == PlaybackStateCompat.STATE_ERROR) {
            return MediaItemHolder.STATE_NONE;
        } else if (pbState.getState() == PlaybackStateCompat.STATE_PLAYING) {
            return  MediaItemHolder.STATE_PLAYING;
        } else {
            return MediaItemHolder.STATE_PAUSED;
        }
    }
}
