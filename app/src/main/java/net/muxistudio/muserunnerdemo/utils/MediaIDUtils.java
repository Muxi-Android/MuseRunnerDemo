package net.muxistudio.muserunnerdemo.utils;

import java.util.Arrays;

//MediaIDs are defined by the User, the form of them are like:
//<categoryType>/<categoryValue>|<musicUniqueId> the two-hierarchy item
public class MediaIDUtils {

    public static final String MEDIA_ID_EMPTY_ROOT = "__EMPTY_ROOT__";
    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final String MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__";

    private static final char CATEGORY_SEPARATOR = '/';
    private static final char LEAF_SPEARATOR = '|';

    public static String[] getHierarchy(String mediaID){
        int pos = mediaID.indexOf(LEAF_SPEARATOR);
        if(pos >= 0){
            mediaID = mediaID.substring(0,pos);
        }
        return mediaID.split(String.valueOf(CATEGORY_SEPARATOR));
    }


    //musicID is like kind of hashcode
    public static String createMediaID(String musicID,String ... categories){
        StringBuilder stringBuilder = new StringBuilder();
        if(categories != null){
            for(int i=0;i<categories.length ;i++){
                if(!isValidCategory(categories[i])){
                    throw new IllegalArgumentException("categories invalid" + categories[i]);
                }
                stringBuilder.append(categories[i]);
                //append '/' at the end of the categories
                if(i < categories.length -1){
                    stringBuilder.append(CATEGORY_SEPARATOR);
                }
            }
        }
        if(musicID != null){
            stringBuilder.append(LEAF_SPEARATOR).append(musicID);
        }
        return stringBuilder.toString();
    }
    private static boolean isValidCategory(String category){
        return category == null || category.indexOf(CATEGORY_SEPARATOR) <0 && category.
                indexOf(LEAF_SPEARATOR) <0;
    }

    public static boolean isBrowseable(String mediaID){
        return mediaID.indexOf(LEAF_SPEARATOR) < 0;
    }

    public static String getParentMediaID(String mediaID){
        String[] hierarchy = getHierarchy(mediaID);
        if(!isBrowseable(mediaID))
            return createMediaID(null, hierarchy);

        if(hierarchy.length <= 1)
            return MEDIA_ID_ROOT;

        String [] parentHierarchy  = Arrays.copyOf(hierarchy,hierarchy.length-1);

        return  createMediaID(null, parentHierarchy);
    }

    public static String extractMusicIDFromMediaID(String mediaID){
        int pos = mediaID.indexOf(LEAF_SPEARATOR);
        if(pos >= 0)
            return mediaID.substring(pos +1);
        return null;
    }
}
