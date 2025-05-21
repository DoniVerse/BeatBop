package com.example.beatbop.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.beatbop.data.entity.*;

@Database(
    entities = {
        User.class,
        Track.class,
        Artist.class,
        Album.class,
        Playlist.class,
        PlaylistTrack.class,
        DownloadedTrack.class
    },
    version = 1,
    exportSchema = false
)
public abstract class BeatBopDatabase extends RoomDatabase {
    private static volatile BeatBopDatabase INSTANCE;

    // DAOs
    public abstract UserDao userDao();
    public abstract TrackDao trackDao();
    public abstract ArtistDao artistDao();
    public abstract AlbumDao albumDao();
    public abstract PlaylistDao playlistDao();
    public abstract PlaylistTrackDao playlistTrackDao();
    public abstract DownloadedTrackDao downloadedTrackDao();

    public static BeatBopDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BeatBopDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            BeatBopDatabase.class,
                            "beatbop_database"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
                }
            }
        }
        return INSTANCE;
    }
} 