package com.example.beatbop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.beatbop.data.entity.Playlist;
import java.util.List;

@Dao
public interface PlaylistDao {
    @Insert
    long insert(Playlist playlist);

    @Update
    void update(Playlist playlist);

    @Delete
    void delete(Playlist playlist);

    @Query("SELECT * FROM playlists WHERE playlistId = :playlistId")
    LiveData<Playlist> getPlaylistById(long playlistId);

    @Query("SELECT * FROM playlists WHERE userId = :userId")
    LiveData<List<Playlist>> getUserPlaylists(long userId);

    @Query("SELECT * FROM playlists WHERE userId = :userId AND name LIKE '%' || :query || '%'")
    LiveData<List<Playlist>> searchUserPlaylists(long userId, String query);

    @Query("SELECT * FROM playlists WHERE isPublic = 1")
    LiveData<List<Playlist>> getPublicPlaylists();

    @Query("UPDATE playlists SET numberOfTracks = numberOfTracks + 1 WHERE playlistId = :playlistId")
    void incrementTrackCount(long playlistId);

    @Query("UPDATE playlists SET numberOfTracks = numberOfTracks - 1 WHERE playlistId = :playlistId")
    void decrementTrackCount(long playlistId);

    @Query("DELETE FROM playlists WHERE playlistId = :playlistId")
    void deleteById(long playlistId);
} 