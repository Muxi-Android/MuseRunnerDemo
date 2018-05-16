package com.example.kolibreath.muserunnerdemo.model;

import java.util.List;

public class MusicMetaData {

    private List<MusicBean> music;

    public List<MusicBean> getMusic() {
        return music;
    }

    public void setMusic(List<MusicBean> music) {
        this.music = music;
    }

    public static class MusicBean {
        /**
         * title : Jazz in Paris
         * album : Jazz & Blues
         * artist : Media Right Productions
         * genre : Jazz & Blues
         * source : Jazz_In_Paris.mp3
         * image : album_art.jpg
         * trackNumber : 1
         * totalTrackCount : 6
         * duration : 103
         * site : https://www.youtube.com/audiolibrary/music
         */

        private String title;
        private String album;
        private String artist;
        private String genre;
        private String source;
        private String image;
        private int trackNumber;
        private int totalTrackCount;
        private int duration;
        private String site;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlbum() {
            return album;
        }

        public void setAlbum(String album) {
            this.album = album;
        }

        public String getArtist() {
            return artist;
        }

        public void setArtist(String artist) {
            this.artist = artist;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getTrackNumber() {
            return trackNumber;
        }

        public void setTrackNumber(int trackNumber) {
            this.trackNumber = trackNumber;
        }

        public int getTotalTrackCount() {
            return totalTrackCount;
        }

        public void setTotalTrackCount(int totalTrackCount) {
            this.totalTrackCount = totalTrackCount;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }
    }
}
