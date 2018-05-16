package com.example.kolibreath.muserunnerdemo.utils;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import java.util.ArrayList;
import java.util.List;


public class QueueUtils {

    private static List<MediaSessionCompat.QueueItem> convertToQueue(
            Iterable<MediaMetadataCompat>tracks, String... categories
    ){
        List<MediaSessionCompat.QueueItem> queue = new ArrayList<>();
        int count = 0;
        for(MediaMetadataCompat metadata: tracks){
            String he
        }
        return null;
    }
}
