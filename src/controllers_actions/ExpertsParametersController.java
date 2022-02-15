package controllers_actions;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ExpertsParametersController {

    @FXML
    private TextField countExperts;

    @FXML
    void startExpertize(ActionEvent event) {
        try {
        	String countOfExperts = countExperts.getText();
        	
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(getClass().getResource("../patterns_actions/InsertAlternativesWindow.fxml"));
        	
            AnchorPane root;
            root = (AnchorPane) loader.load();
            
            InsertAlternativesWindowController insertAlternativesWindowController = loader.getController();
            insertAlternativesWindowController.prepareTable(countOfExperts);
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}