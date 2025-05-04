package ServerSide;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class TFIDFProcessor {

    public static void processSongsForTFIDF(String jsonFilePath) throws IOException {
        // Read the JSON file
        Gson gson = new Gson();
        Reader reader = Files.newBufferedReader(Paths.get(jsonFilePath));
        List<Song> songs = Arrays.asList(gson.fromJson(reader, Song[].class));

        // Extract song names
        List<String> songNames = new ArrayList<>();
        for (Song song : songs.subList(0, 500)) {  // Limit to 500 songs
            songNames.add(song.getName());
        }

        // Calculate TF-IDF for songs
        Map<String, Double> tfIdfScores = calculateTFIDF(songNames);

        // Display
        System.out.println("TF-IDF Scores:");
        for (Map.Entry<String, Double> entry : tfIdfScores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Calculates TF-IDF
    public static Map<String, Double> calculateTFIDF(List<String> documents) {
        // Calculates Term Frequency for each song name
        Map<String, Integer> termFrequency = new HashMap<>();
        for (String doc : documents) {
            String[] words = doc.split("\\s+");
            for (String word : words) {
                word = word.toLowerCase();
                termFrequency.put(word, termFrequency.getOrDefault(word, 0) + 1);
            }
        }

        // Calculates Inverse Document Frequency
        Map<String, Double> idfScores = new HashMap<>();
        Set<String> uniqueWords = new HashSet<>();
        for (String doc : documents) {
            String[] words = doc.split("\\s+");
            Set<String> wordsInDoc = new HashSet<>(Arrays.asList(words));
            uniqueWords.addAll(wordsInDoc);
        }

        int totalDocuments = documents.size();
        for (String word : uniqueWords) {
            int documentCount = 0;
            for (String doc : documents) {
                if (doc.contains(word)) {
                    documentCount++;
                }
            }
            double idf = Math.log((double) totalDocuments / (documentCount + 1));
            idfScores.put(word, idf);
        }

        // Calculates TF-IDF
        Map<String, Double> tfIdfScores = new HashMap<>();
        for (String word : termFrequency.keySet()) {
            double tf = (double) termFrequency.get(word) / documents.size();
            double idf = idfScores.getOrDefault(word, 0.0);
            tfIdfScores.put(word, tf * idf);
        }

        return tfIdfScores;
    }
}
