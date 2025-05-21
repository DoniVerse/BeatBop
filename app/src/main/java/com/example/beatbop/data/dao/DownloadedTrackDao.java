package com.example.beatbop.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.example.beatbop.data.entity.DownloadedTrack;
import java.util.List;

@Dao
public interface DownloadedTrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DownloadedTrack downloadedTrack);

    @Update
    void update(DownloadedTrack downloadedTrack);

    @Delete
    void delete(DownloadedTrack downloadedTrack);

    @Query("SELECT * FROM downloaded_tracks WHERE downloadId = :downloadId")
    LiveData<DownloadedTrack> getDownloadById(long downloadId);

    @Query("SELECT * FROM downloaded_tracks WHERE userId = :userId")
    LiveData<List<DownloadedTrack>> getUserDownloads(long userId);

    @Query("SELECT * FROM downloaded_tracks WHERE trackId = :trackId AND userId = :userId")
    LiveData<DownloadedTrack> getDownloadedTrack(long trackId, long userId);

    @Query("SELECT * FROM downloaded_tracks WHERE isComplete = 1 AND userId = :userId")
    LiveData<List<DownloadedTrack>> getCompletedDownloads(long userId);

    @Query("SELECT * FROM downloaded_tracks WHERE isComplete = 0 AND userId = :userId")
    LiveData<List<DownloadedTrack>> getPendingDownloads(long userId);

    @Query("UPDATE downloaded_tracks SET playCount = playCount + 1, lastPlayedDate = :date WHERE downloadId = :downloadId")
    void incrementPlayCount(long downloadId, String date);

    @Query("DELETE FROM downloaded_tracks WHERE downloadId = :downloadId")
    void deleteById(long downloadId);

    @Query("DELETE FROM downloaded_tracks WHERE userId = :userId")
    void clearUserDownloads(long userId);
} 