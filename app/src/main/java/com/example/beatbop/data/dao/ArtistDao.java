package com.example.beatbop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.beatbop.data.entity.Artist;
import java.util.List;

@Dao
public interface ArtistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Artist artist);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Artist> artists);

    @Update
    void update(Artist artist);

    @Delete
    void delete(Artist artist);

    @Query("SELECT * FROM artists WHERE artistId = :artistId")
    LiveData<Artist> getArtistById(long artistId);

    @Query("SELECT * FROM artists WHERE name LIKE '%' || :query || '%'")
    LiveData<List<Artist>> searchArtists(String query);

    @Query("SELECT * FROM artists ORDER BY numberOfFans DESC LIMIT :limit")
    LiveData<List<Artist>> getPopularArtists(int limit);

    @Query("SELECT * FROM artists ORDER BY numberOfTracks DESC")
    LiveData<List<Artist>> getArtistsByTrackCount();

    @Query("SELECT * FROM artists ORDER BY numberOfAlbums DESC")
    LiveData<List<Artist>> getArtistsByAlbumCount();

    @Query("DELETE FROM artists WHERE artistId = :artistId")
    void deleteById(long artistId);
} 