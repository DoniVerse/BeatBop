package com.example.beatbop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.beatbop.data.entity.Track;
import java.util.List;

@Dao
public interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Track track);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Track> tracks);

    @Update
    void update(Track track);

    @Delete
    void delete(Track track);

    @Query("SELECT * FROM tracks WHERE trackId = :trackId")
    LiveData<Track> getTrackById(long trackId);

    @Query("SELECT * FROM tracks WHERE artistId = :artistId")
    LiveData<List<Track>> getTracksByArtist(long artistId);

    @Query("SELECT * FROM tracks WHERE albumId = :albumId")
    LiveData<List<Track>> getTracksByAlbum(long albumId);

    @Query("SELECT * FROM tracks WHERE title LIKE '%' || :query || '%'")
    LiveData<List<Track>> searchTracks(String query);

    @Query("SELECT * FROM tracks ORDER BY rank DESC LIMIT :limit")
    LiveData<List<Track>> getTopTracks(int limit);

    @Query("SELECT * FROM tracks WHERE isExplicit = 0")
    LiveData<List<Track>> getNonExplicitTracks();

    @Query("DELETE FROM tracks WHERE trackId = :trackId")
    void deleteById(long trackId);

    @Query("SELECT * FROM tracks WHERE releaseDate >= :date")
    LiveData<List<Track>> getTracksReleasedAfter(String date);
} 