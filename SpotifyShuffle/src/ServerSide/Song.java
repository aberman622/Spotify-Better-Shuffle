package ServerSide;

import java.util.List;
import java.util.stream.Collectors;

public class Song {
    private String name;
    private String id;
    private String uri;
    private Album album;
    private List<Artist> artists;

    public Song(String name, String id, String uri, Album album, List<Artist> artists) {
        this.name = name;
        this.id = id;
        this.uri = uri;
        this.album = album;
        this.artists = artists;
    }

    // Getters for accessing data
    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public Album getAlbum() {
        return album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    // Inner classes for Album and Artist
    public static class Album {
        private String name;

        public Album(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public static class Artist {
        private String name;

        public Artist(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    @Override
    public String toString() {
        String artistNames = artists.stream()
                .map(Artist::getName)
                .collect(Collectors.joining(", "));
        return "Song{" +
                "name='" + name + '\'' +
                ", artists=" + artistNames +
                ", album=" + album.getName() +
                '}';
    }
}
