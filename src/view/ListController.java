/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import app.Song;
import app.SongLibrary;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author shanepark
 */
public class ListController implements Initializable {
    
    @FXML private Label songLibraryLabel;
    @FXML private Label selectedSongLabel;
    @FXML private Label newSongLabel;
    
    @FXML ListView<String> listView;
    
    @FXML private TextField newName;
    @FXML private TextField newArtist;
    @FXML private TextField newAlbum;
    @FXML private TextField newYear;
    
    @FXML private TextField selectedName;
    @FXML private TextField selectedArtist;
    @FXML private TextField selectedAlbum;
    @FXML private TextField selectedYear;
    
    @FXML private Button btn_add;
    @FXML private Button btn_edit;
    @FXML private Button btn_delete;
    
    private ObservableList<String> obsList;
    
    @FXML
    private void handleAdd(ActionEvent event) {
        String name = newName.getText();
        String artist = newArtist.getText();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText("Are you sure you want to add the song \"" + name.trim() + "\" by \"" + artist.trim() + "\" to your library?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK){
            String album = newAlbum.getText();
            String year = newYear.getText();
            int code = SongLibrary.addSong(name.trim(), artist.trim(), album.trim(), year.trim());
            
            if (code < 0) {
                /* -1: duplicate
                * -2: invalid format
                * -3: year not numerical
                */
                this.handleErrorDialogs(code);
            }else {
                //Successful add
                clearNSInputs();
                loadSongList();
                listView.getSelectionModel().select(code);
            }
        }
    }
    @FXML
    private void handleEdit(ActionEvent event) {
        int index = listView.getSelectionModel().getSelectedIndex();
        String nName = selectedName.getText();
        String nArtist = selectedArtist.getText();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText("Are you sure you want to edit the song \"" + SongLibrary.getSongList().get(index).getName() + "\" by \"" + SongLibrary.getSongList().get(index).getArtist() + "\" to \"" + nName.trim() + "\" by \"" + nArtist.trim() + "\"?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK){
            String nAlbum = selectedAlbum.getText();
            String nYear = selectedYear.getText();
            
            Song s = SongLibrary.getSongList().get(index);
            index = SongLibrary.editSong(index, nName.trim(), nArtist.trim(), nAlbum.trim(), nYear.trim());
            if (index < 0){ //if invalid edit - readd prev song
                this.handleErrorDialogs(index);
                index = SongLibrary.add(s);
            }
            loadSongList();
            listView.getSelectionModel().select(index);
        }
    }
    @FXML
    private void handleDelete(ActionEvent event) {
        int index = listView.getSelectionModel().getSelectedIndex();
        
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Confirmation Required");
        alert.setHeaderText("Are you sure you want to delete the song \"" + SongLibrary.getSongList().get(index).getName() + "\" by \"" + SongLibrary.getSongList().get(index).getArtist() + "\" from your library?");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get() == ButtonType.OK){
            index = SongLibrary.deleteSong(index);
            loadSongList();
            if (index == -1){   //no song left in list
                clearSSInputs();
            }else {
                listView.getSelectionModel().select(index);
            }
        }
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        songLibraryLabel.setText("Song Library");
        selectedSongLabel.setText("Selected Song's Info");
        newSongLabel.setText("New Song's Info");
        songLibraryLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
        selectedSongLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
        newSongLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 15));
    }
    
    public void start(Stage stage){
        loadSongList();
        
        listView.getSelectionModel().select(0);
        if (!SongLibrary.getSongList().isEmpty()){
            selectedName.setText(SongLibrary.getSongList().get(0).getName());
            selectedArtist.setText(SongLibrary.getSongList().get(0).getArtist());
            selectedAlbum.setText(SongLibrary.getSongList().get(0).getAlbum());
            if (SongLibrary.getSongList().get(0).getYear() == 0){
                selectedYear.setText("");
            }else{
                selectedYear.setText(String.valueOf(SongLibrary.getSongList().get(0).getYear()));
            }
        }else {
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
        }
        //create listener to dynamically repopulate text field based on obslist index
        listView.getSelectionModel().selectedIndexProperty().addListener((obs,oldVal,newVal) -> populateSelectedTF());
        //dynamically only enable add button when valid song name and artist is inputted to the text field
        btn_add.disableProperty().bind(Bindings.createBooleanBinding(() -> newName.getText().trim().isEmpty() || newArtist.getText().trim().isEmpty(), newName.textProperty(), newArtist.textProperty()));
    }
    
    private void populateSelectedTF(){
        int index = listView.getSelectionModel().getSelectedIndex();
        
        if (index>=0){
            selectedName.setText(SongLibrary.getSongList().get(index).getName());
            selectedArtist.setText(SongLibrary.getSongList().get(index).getArtist());
            selectedAlbum.setText(SongLibrary.getSongList().get(index).getAlbum());
            if (SongLibrary.getSongList().get(index).getYear() == 0){
                selectedYear.setText("");
            }else{
                selectedYear.setText(String.valueOf(SongLibrary.getSongList().get(index).getYear()));
            }
        }
    }
    
    //populate song library list
    private void loadSongList() {
        obsList = FXCollections.observableArrayList(SongLibrary.getPartialSongList());
        listView.setItems(obsList);
        
        if (obsList.isEmpty()){ //disable edit and delete btn if list empty
            btn_edit.setDisable(true);
            btn_delete.setDisable(true);
        }else {
            btn_edit.setDisable(false);
            btn_delete.setDisable(false);
        }
    }
    
    //handle all errors using appropriate dialogs based on the code
    private void handleErrorDialogs(int code) {
        Alert alert = new Alert(AlertType.ERROR);
	alert.initModality(Modality.APPLICATION_MODAL);
        
        /* -1: duplicate
        * -2: invalid format
        * -3: year not numerical
        */
        switch (code) {
            case -1:
                alert.setTitle("Song Could Not be Added/Edited Due to Duplicate");
                alert.setHeaderText("This song name and artist already exists in the library.");
                break;
            case -3:
                alert.setTitle("Song Could Not be Added/Edited Due to Incorrect Year Format");
                alert.setHeaderText("Year must be a valid number.");
                break;
            default:    //-2
                alert.setTitle("Song Could Not be Added/Edited Due to Invalid Formatting");
                alert.setHeaderText("Song must contain a valid song name and a song artist.");
                break;
        }
        alert.showAndWait();
    }

    private void clearNSInputs() {
        newName.clear();
        newArtist.clear();
        newAlbum.clear();
        newYear.clear();
    }
    
    private void clearSSInputs() {
        selectedName.clear();
        selectedArtist.clear();
        selectedAlbum.clear();
        selectedYear.clear();
    }
}
