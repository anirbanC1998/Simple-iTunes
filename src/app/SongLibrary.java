/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author shanepark
 */
public class SongLibrary {
    private static List<Song> songLibrary = new ArrayList<Song>();
    private static String filePath;
    
    //create new csv file if it does not exist
    public static void initCSVFile(File csvFile){
        try {
            csvFile.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(SongLibrary.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void loadSongs(){
        File csvFile = new File(filePath);
        if (!csvFile.isFile()){ //check if csv file exists
            SongLibrary.initCSVFile(csvFile);
        }
        
        try (BufferedReader csvReader = new BufferedReader(new FileReader(filePath))) {
            String row;
            while ((row = csvReader.readLine()) != null){
                String[] song = row.split(",");
                
                Song s = null;
                switch (song.length) {
                    case 2:
                        s = new Song(song[0], song[1]);
                        break;
                    case 3:
                        s = new Song(song[0], song[1], song[2]);
                        break;
                    case 4:
                        s = new Song(song[0], song[1], song[2], Integer.valueOf(song[3]));
                        break;
                    default:
                        System.out.println("Error: Invalid song format");
                        break;
                }
                if (s != null){
//                    System.out.println(s);
                    songLibrary.add(s);
                }
                
            }
        }catch(IOException e){ Logger.getLogger(SongLibrary.class.getName()).log(Level.SEVERE, null, e); }
    }
    
    public static int add(Song s){
        songLibrary.add(s);
        Collections.sort(songLibrary);
//        songLibrary.forEach((song) -> {
//            System.out.println(song);
//        });
        
        return findSongIndex(s);
    }
    
    //find and return index of newly added song
    private static int findSongIndex(Song s){
        int i = 0;
        for (; !songLibrary.get(i).equals(s); i++){ }
        return i;
    }
    
    public static int addSong(String name, String artist, String album, String year){
        if (name.equals("") || artist.equals("")){
            //error: invalid format dialog = -2
//            System.out.println("Invalid format!");
            return -2;
        }else{
            //check duplicate -> proceed with add
            if (hasDuplicate(name, artist)){
//                System.out.println("Duplicate!");
                //error: duplicate = -1
                return -1;
            }else{
                if (!year.equals("")){
                    try {
                        int numYear = Integer.parseInt(year.trim());
                        return add(new Song(name, artist, album, numYear));
                    }catch (NumberFormatException nfe){ return -3;/*Error: Year must be a valid number!*/ }
                }else if (!album.equals("")){
                    return add(new Song(name, artist, album));
                }else{
                    return add(new Song(name, artist));
                }
            }
        }
    }
    
    public static int editSong(int index, String nName, String nArtist, String nAlbum, String nYear) {
        deleteSong(index);
        return addSong(nName, nArtist, nAlbum, nYear);
    }
    
    public static int deleteSong(int index) {
        songLibrary.remove(index);
        if (songLibrary.isEmpty()){    //if no song left in list after deletion
            return -1;
        }
        
        if (index == songLibrary.size()){   //if last song deleted
            return index-1;
        }
        return index;
    }
    
    public static List<Song> getSongList(){
        return songLibrary;
    }
    
    //get song list to display song name and artist on listview
    public static List<String> getPartialSongList(){
        List<String> stringSongLib = new ArrayList<>(songLibrary.size());
        
        songLibrary.forEach((song) -> {
            stringSongLib.add(song.toPartialString());
        });
        return stringSongLib;
    }
    
    //check if duplicate songs exist
    public static boolean hasDuplicate(String name, String artist){
        return songLibrary.stream().map((s) -> s.compareTo(new Song(name, artist))).anyMatch((dup) -> (dup == 0));
    }
    
    public static void setFilePath(String filePath){
        SongLibrary.filePath = filePath;
    }
    
    public static void saveToFile() throws IOException {
        try (FileWriter csvWriter = new FileWriter(filePath)) {
            songLibrary.forEach((song) -> {
                try {
                    csvWriter.write(song.toString(song));
                } catch (IOException ex) {
                    Logger.getLogger(SongLibrary.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            csvWriter.flush();
        }
    }
}
