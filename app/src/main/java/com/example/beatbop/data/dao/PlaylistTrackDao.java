package com.example.beatbop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.beatbop.data.entity.PlaylistTrack;
import com.example.beatbop.data.entity.Track;
import java.util.List;

@Dao
public interface PlaylistTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PlaylistTrack playlistTrack);

    @Delete
    void delete(PlaylistTrack playlistTrack);

    @Query("SELECT * FROM playlist_tracks WHERE playlistId = :playlistId ORDER BY position")
    LiveData<List<PlaylistTrack>> getPlaylistTracks(long playlistId);

    @Query("SELECT t.* FROM tracks t INNER JOIN playlist_tracks pt ON t.trackId = pt.trackId WHERE pt.playlistId = :playlistId ORDER BY pt.position")
    LiveData<List<Track>> getTracksInPlaylist(long playlistId);

    @Query("SELECT COUNT(*) FROM playlist_tracks WHERE playlistId = :playlistId")
    int getTrackCount(long playlistId);

    @Query("SELECT MAX(position) FROM playlist_tracks WHERE playlistId = :playlistId")
    int getLastPosition(long playlistId);

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId AND trackId = :trackId")
    void removeTrackFromPlaylist(long playlistId, long trackId);

    @Query("DELETE FROM playlist_tracks WHERE playlistId = :playlistId")
    void clearPlaylist(long playlistId);

    @Transaction
    @Query("UPDATE playlist_tracks SET position = position - 1 WHERE playlistId = :playlistId AND position > :position")
    void reorderAfterRemoval(long playlistId, int position);
} 