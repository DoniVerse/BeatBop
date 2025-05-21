package com.example.beatbop.data.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "playlist_tracks",
        primaryKeys = {"playlistId", "trackId"},
        foreignKeys = {
                @ForeignKey(
                        entity = Playlist.class,
                        parentColumns = "playlistId",
                        childColumns = "playlistId",
                        onDelete = ForeignKey.CASCADE
                ),
                @ForeignKey(
                        entity = Track.class,
                        parentColumns = "trackId",
                        childColumns = "trackId",
                        onDelete = ForeignKey.CASCADE
                )
        })
public class PlaylistTrack {
    private long playlistId;
    private long trackId;
    private int position;
    private String dateAdded;

    // Getters and Setters
    public long getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(long playlistId) {
        this.playlistId = playlistId;
    }

    public long getTrackId() {
        return trackId;
    }

    public void setTrackId(long trackId) {
        this.trackId = trackId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
} 