package com.example.beatbop;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class MusicListActivity extends AppCompatActivity implements TrackAdapter.OnTrackClickListener {

    private static final String TAG = "MusicListActivity";
    private SearchView searchView;
    private RecyclerView artistsRecyclerView;
    private RecyclerView tracksRecyclerView;
    private TrackAdapter trackAdapter;
    private ArtistAdapter artistAdapter;
    private View searchResultsLayout;
    private ApiInterface apiInterface;
    private MediaPlayer mediaPlayer;
    private TextView nowPlayingText;
    private ImageButton playPauseButton;
    private ImageButton stopButton;
    private Track currentTrack;
    private boolean isAutoRepeat = true;

    private final List<Artist> popularArtists = Arrays.asList(
        createArtist("Eminem", "https://e-cdns-images.dzcdn.net/images/artist/19cc38f9d69b352f718782e7a22f9c32/250x250-000000-80-0-0.jpg"),
        createArtist("Ed Sheeran", "https://e-cdns-images.dzcdn.net/images/artist/2a03401e091893ec8abd8f15426b1147/250x250-000000-80-0-0.jpg"),
        createArtist("Taylor Swift", "https://e-cdns-images.dzcdn.net/images/artist/8e45f6d855d66828fa80bc9bbb4935ae/250x250-000000-80-0-0.jpg"),
        createArtist("Drake", "https://e-cdns-images.dzcdn.net/images/artist/5d2fa7f140a6bdc2c864c3465a61fc71/250x250-000000-80-0-0.jpg"),
        createArtist("The Weeknd", "https://e-cdns-images.dzcdn.net/images/artist/033d460f704896c9caca89a1d753a137/250x250-000000-80-0-0.jpg"),
        createArtist("Rihanna", "https://e-cdns-images.dzcdn.net/images/artist/7d514d87a186c02657a8e88a84de36f2/250x250-000000-80-0-0.jpg")
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_list);

        initializeViews();
        setupRecyclerViews();
        setupClickListeners();
        setupDeezerApi();
        setupMediaPlayer();
    }

    private void initializeViews() {
        searchView = findViewById(R.id.searchView);
        artistsRecyclerView = findViewById(R.id.artistsRecyclerView);
        tracksRecyclerView = findViewById(R.id.tracksRecyclerView);
        searchResultsLayout = findViewById(R.id.searchResultsLayout);
        nowPlayingText = findViewById(R.id.nowPlayingText);
        playPauseButton = findViewById(R.id.playPauseButton);
        stopButton = findViewById(R.id.stopButton);
    }

    private void setupRecyclerViews() {
        // Setup Artists RecyclerView
        artistsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        artistAdapter = new ArtistAdapter(popularArtists, artist -> searchTracks(artist.getName()));
        artistsRecyclerView.setAdapter(artistAdapter);

        // Setup Tracks RecyclerView
        tracksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trackAdapter = new TrackAdapter(new ArrayList<>(), this);
        tracksRecyclerView.setAdapter(trackAdapter);
    }

    private void setupClickListeners() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTracks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        playPauseButton.setOnClickListener(v -> togglePlayPause());
        stopButton.setOnClickListener(v -> stopPlayback());
    }

    private void setupDeezerApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://deezerdevs-deezer.p.rapidapi.com")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    private void setupMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            if (isAutoRepeat && currentTrack != null) {
                // Auto-repeat the track
                playTrack(currentTrack);
            } else {
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
                nowPlayingText.setText("Playback completed");
            }
        });

        mediaPlayer.setOnPreparedListener(mp -> {
            mp.start();
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            nowPlayingText.setText("Now Playing: " + currentTrack.getTitle() + "\n(30 sec preview)");
        });

        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            Log.e(TAG, "MediaPlayer error: " + what + ", " + extra);
            Toast.makeText(this, "Error playing track", Toast.LENGTH_SHORT).show();
            return false;
        });
    }

    private void searchTracks(String query) {
        artistsRecyclerView.setVisibility(View.GONE);
        searchResultsLayout.setVisibility(View.VISIBLE);

        Call<mydata> retrofitData = apiInterface.searchTracks(query);
        retrofitData.enqueue(new Callback<mydata>() {
            @Override
            public void onResponse(Call<mydata> call, Response<mydata> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mydata data = response.body();
                    if (data.getData() != null && !data.getData().isEmpty()) {
                        trackAdapter.updateTracks(data.getData());
                        Log.d(TAG, "Found " + data.getData().size() + " tracks");
                    } else {
                        Toast.makeText(MusicListActivity.this, "No tracks found", Toast.LENGTH_SHORT).show();
                        trackAdapter.updateTracks(new ArrayList<>());
                    }
                } else {
                    String errorMessage = "Error: " + response.code();
                    Log.e(TAG, errorMessage);
                    Toast.makeText(MusicListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<mydata> call, Throwable t) {
                String errorMessage = "Network error: " + t.getMessage();
                Log.e(TAG, errorMessage, t);
                Toast.makeText(MusicListActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onTrackClick(Track track) {
        if (track.getPreview() != null) {
            currentTrack = track;
            playTrack(track);
        } else {
            Toast.makeText(this, "No preview available for this track", Toast.LENGTH_SHORT).show();
        }
    }

    private void playTrack(Track track) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(track.getPreviewUrl());
            mediaPlayer.prepareAsync(); // Using async preparation for better performance
            currentTrack = track;
        } catch (IOException e) {
            Log.e(TAG, "Error playing track", e);
            Toast.makeText(this, "Error playing track", Toast.LENGTH_SHORT).show();
        }
    }

    private void togglePlayPause() {
        if (mediaPlayer != null && currentTrack != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            } else {
                mediaPlayer.start();
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
            }
        }
    }

    private void stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
            nowPlayingText.setText("Playback stopped");
        }
    }

    private Artist createArtist(String name, String pictureUrl) {
        Artist artist = new Artist();
        artist.setName(name);
        artist.setPictureMedium(pictureUrl);
        return artist;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && currentTrack != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
        }
    }
} 