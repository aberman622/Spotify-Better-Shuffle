package ServerSide;

import java.util.*;

public class PlaylistGenerator {

    public static List<Song> generatePlaylist(List<Song> songs, Map<String, Double> tfIdfScores) {
        // Ranks songs based on the sum of TF-IDF scores
        List<Song> rankedSongs = new ArrayList<>(songs);
        rankedSongs.sort((song1, song2) -> {
            double score1 = calculateSongTFIDFScore(song1, tfIdfScores);
            double score2 = calculateSongTFIDFScore(song2, tfIdfScores);
            return Double.compare(score2, score1);
        });

        return rankedSongs;
    }

    // Calculates the TF-IDF score for a song by summing the TF-IDF scores
    private static double calculateSongTFIDFScore(Song song, Map<String, Double> tfIdfScores) {
        double score = 0.0;
        String[] words = song.getName().split("\\s+");
        for (String word : words) {
            word = word.toLowerCase();
            score += tfIdfScores.getOrDefault(word, 0.0);
        }
        return score;
    }
}
