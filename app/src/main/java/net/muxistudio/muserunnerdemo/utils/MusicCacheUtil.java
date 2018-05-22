package net.muxistudio.muserunnerdemo.utils;

import android.content.Context;

import com.danikula.videocache.HttpProxyCacheServer;

import net.muxistudio.muserunnerdemo.App;

import java.io.File;
import java.io.IOException;

/**
 * AndroidVideoCache 缓存音乐
 */

public class MusicCacheUtil {

    public static void init(String musicUrl){
        HttpProxyCacheServer proxy = getProxy();
        String proxyUrl = proxy.getProxyUrl(musicUrl);

    }

    public static HttpProxyCacheServer getProxy(){
        return App.getProxy(new App());
    }

    public static HttpProxyCacheServer newProxy(Context context){
        return new HttpProxyCacheServer.Builder(context)
                .maxCacheFilesCount(20)
        //      .diskUsage()
                .build();

    }

    public static File getMusicCacheDir(Context context){
        return new File(context.getExternalCacheDir(),"music-cache");
    }

    public static void cleanMusicCacheDir(Context context) throws IOException {
        File musicCache = getMusicCacheDir(context);
        cleanDirectory(musicCache);
    }

    private static void cleanDirectory(File file) throws IOException {
        if (!file.exists()){
            return;
        }
        File[] contentFiles = file.listFiles();
        if (contentFiles != null){
            for (File contentFile : contentFiles) {
                delete(contentFile);
            }
        }
    }

    private static void delete(File file) throws IOException {
        if (file.isFile() && file.exists()){
            deleteOrThrow(file);
        }else {
             cleanDirectory(file);
             deleteOrThrow(file);
        }
    }

    private static void deleteOrThrow(File file) throws IOException {
        if (file.exists()){
            boolean isDeleted = file.delete();
            if (!isDeleted){
                throw new IOException(String.format("File %s can't be deleted",file.getAbsolutePath()));
            }
        }
    }
}
