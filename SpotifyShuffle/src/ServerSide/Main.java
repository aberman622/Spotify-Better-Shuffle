package ServerSide;

import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: First fetch songs and save them to songs.json
            SpotifyAPI.fetchAllSongs("Shut Up and Dance");  // You can change the query as needed

            // Step 2: Process songs for TF-IDF
            TFIDFProcessor.processSongsForTFIDF("songs.json");

            // Step 3: Get all fetched songs
            List<Song> songs = new ArrayList<>(SpotifyAPI.allSongs);

            if (songs.isEmpty()) {
                System.out.println("No songs were fetched. Exiting.");
                return;
            }

            // Step 4: Extract song names
            int maxSongs = Math.min(500, songs.size());
            List<String> songNames = new ArrayList<>();
            for (Song song : songs.subList(0, maxSongs)) {
                songNames.add(song.getName());
            }

            // Step 5: Calculate TF-IDF scores
            Map<String, Double> tfIdfScores = TFIDFProcessor.calculateTFIDF(songNames);

            // Step 6: Generate Playlist based on TF-IDF scores
            List<Song> playlist = PlaylistGenerator.generatePlaylist(songs, tfIdfScores);

            // Step 7: Output the generated playlist
            System.out.println("\nGenerated Playlist:");
            for (Song song : playlist) {
                System.out.println(song);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}