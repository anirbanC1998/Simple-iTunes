/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 *
 * @author shanepark
 */
public class ListController implements Initializable {
    
    @FXML private Label songLibraryLabel;
    @FXML private Label selectedSongLabel;
    @FXML private Label newSongLabel;
    
    @FXML ListView<String> listView;
    
    @FXML
    private void handleAdd(ActionEvent event) {
        System.out.println("You clicked me!");
//        label.setText("Hello World!");
    }
    @FXML
    private void handleEdit(ActionEvent event) {
        System.out.println("You clicked me!");
    }
    @FXML
    private void handleDelete(ActionEvent event) {
        System.out.println("You clicked me!");
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
    
}
