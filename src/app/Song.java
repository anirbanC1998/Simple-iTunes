/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author shanepark
 */
public class Song implements Comparable<Song> {

    private String name;
    private String artist;
    private String album;
    private int year;
    
    public Song(String name, String artist) {
        this.name = name;
        this.artist = artist;
        this.album = "";
        this.year = 0;
    }

    public Song(String name, String artist, String album) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = 0;
    }

    public Song(String name, String artist, int year) {
        this.name = name;
        this.artist = artist;
        this.album = "";
        this.year = year;
    }

    public Song(String name, String artist, String album, int year) {
        this.name = name;
        this.artist = artist;
        this.album = album;
        this.year = year;
    }

    @Override
    public String toString() {
        return name + ", " + artist + ", " + album + ", " + year;
    }
    
    //for saving to csv file in correct format
    public String toString(Song s){
        if (s.getAlbum() == null && s.getYear() == 0){
            return name + "," + artist + "," + "," + "\n";
        }else if (s.getAlbum() == null){
            return name + "," + artist + "," + "," + year + "\n";
        }else if (s.getYear() == 0){
            return name + "," + artist + "," + album + "," + "\n";
        }
        return name + "," + artist + "," + album + "," + year + "\n";
    }
    
    //for displaying on obs list
    public String toPartialString() {
        return name + ", " + artist;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public int compareTo(Song song) {
        int comparison = this.name.compareToIgnoreCase(song.getName());
        
        //if song name is same, compare artist names
        if (comparison == 0) { comparison = this.artist.compareToIgnoreCase(song.getArtist()); }
        
        return comparison;
    }
    
    
}
