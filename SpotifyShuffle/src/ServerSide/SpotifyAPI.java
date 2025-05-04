package ServerSide;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class SpotifyAPI {

    // List to store all songs
    static List<Song> allSongs = new ArrayList<>();

    // Fetch song data from Spotify API
    public static Song parseSongData(JsonObject trackJson) {
        if (trackJson == null) {
            System.out.println("Track object is null for: " + trackJson);
            return null;
        }

        // Parse album data
        JsonObject albumJson = trackJson.getAsJsonObject("album");
        Song.Album album = new Song.Album(albumJson != null && albumJson.has("name") ? albumJson.get("name").getAsString() : "Unknown Album");

        // Parse artists
        JsonArray artistsJson = trackJson.getAsJsonArray("artists");
        List<Song.Artist> artists = new ArrayList<>();
        for (JsonElement artistElement : artistsJson) {
            String artistName = artistElement.getAsJsonObject().get("name").getAsString();
            Song.Artist artist = new Song.Artist(artistName);  // Use the constructor of Artist
            artists.add(artist);
        }

        // Extract song information
        String name = trackJson.has("name") ? trackJson.get("name").getAsString() : "Unknown Song";
        String id = trackJson.has("id") ? trackJson.get("id").getAsString() : "Unknown ID";
        String uri = trackJson.has("uri") ? trackJson.get("uri").getAsString() : "Unknown URI";

        return new Song(name, id, uri, album, artists);  // Create a Song object
    }

    // Fetch all songs and store them in a list
    public static void fetchAllSongs(String query) throws IOException {
        int offset = 0;
        int limit = 50;
        boolean hasNext = true;

        // Get the access token
        String accessToken = SpotifyAuth.getAccessToken();

        while (hasNext) {
            String urlString = "https://api.spotify.com/v1/search?q=" + URLEncoder.encode(query, "UTF-8") + "&type=track&limit=" + limit + "&offset=" + offset;
            URL url = new URL(urlString);

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", "Bearer " + accessToken);  // Using the dynamic token

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            JsonObject responseJson = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray songs = responseJson.getAsJsonObject("tracks").getAsJsonArray("items");

            // Process each song and print it out
            for (JsonElement songElement : songs) {
                Song song = parseSongData(songElement.getAsJsonObject());
                if (song != null) {
                    System.out.println(song);
                    allSongs.add(song);
                }
            }

            // Check next page
            JsonObject tracksObject = responseJson.getAsJsonObject("tracks");
            hasNext = tracksObject.has("next") && !tracksObject.get("next").isJsonNull();
            offset += limit;  // Move next page
        }



        // write to JSON file
        writeSongsToJsonFile();
    }

    private static void writeSongsToJsonFile() {
        try (Writer writer = new FileWriter("songs.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(allSongs, writer);
            System.out.println("All songs have been written to songs.json");
        } catch (IOException e) {
            System.out.println("Error writing songs to file: " + e.getMessage());
        }
    }
}


