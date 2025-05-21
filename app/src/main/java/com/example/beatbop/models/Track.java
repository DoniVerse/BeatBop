package com.example.beatbop.models;

public class Track {
    private String id;
    private String title;
    private String artist;
    private String albumArtUrl;
    private String streamUrl;
    private int duration;

    public Track(String id, String title, String artist, String albumArtUrl, String streamUrl, int duration) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.albumArtUrl = albumArtUrl;
        this.streamUrl = streamUrl;
        this.duration = duration;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public String getAlbumArtUrl() { return albumArtUrl; }
    public String getStreamUrl() { return streamUrl; }
    public int getDuration() { return duration; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setArtist(String artist) { this.artist = artist; }
    public void setAlbumArtUrl(String albumArtUrl) { this.albumArtUrl = albumArtUrl; }
    public void setStreamUrl(String streamUrl) { this.streamUrl = streamUrl; }
    public void setDuration(int duration) { this.duration = duration; }
} 