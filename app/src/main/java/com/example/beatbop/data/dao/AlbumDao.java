package com.example.beatbop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.beatbop.data.entity.Album;
import java.util.List;

@Dao
public interface AlbumDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Album album);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Album> albums);

    @Update
    void update(Album album);

    @Delete
    void delete(Album album);

    @Query("SELECT * FROM albums WHERE albumId = :albumId")
    LiveData<Album> getAlbumById(long albumId);

    @Query("SELECT * FROM albums WHERE artistId = :artistId")
    LiveData<List<Album>> getAlbumsByArtist(long artistId);

    @Query("SELECT * FROM albums WHERE title LIKE '%' || :query || '%'")
    LiveData<List<Album>> searchAlbums(String query);

    @Query("SELECT * FROM albums ORDER BY releaseDate DESC")
    LiveData<List<Album>> getRecentAlbums();

    @Query("SELECT * FROM albums WHERE genre = :genre")
    LiveData<List<Album>> getAlbumsByGenre(String genre);

    @Query("SELECT * FROM albums ORDER BY duration DESC")
    LiveData<List<Album>> getLongestAlbums();

    @Query("DELETE FROM albums WHERE albumId = :albumId")
    void deleteById(long albumId);
} 