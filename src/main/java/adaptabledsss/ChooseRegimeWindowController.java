package adaptabledsss;

import controllers_actions.CreateEntityController;
import controllers_actions.ExpertsParametersController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ChooseRegimeWindowController {

    @FXML
    void createCriteria(ActionEvent event) {
        try {
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(getClass().getResource("/controllers_actions/CreateEntity.fxml"));

            AnchorPane root;
            root = (AnchorPane) loader.load();
            
            CreateEntityController createEntityController = loader.getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void inutAlternatives(ActionEvent event) {
        try {
        	FXMLLoader loader = new FXMLLoader();
        	loader.setLocation(getClass().getResource("/controllers_actions/ExpertsParameters.fxml"));
        	
            AnchorPane root;
            root = (AnchorPane) loader.load();
            
            ExpertsParametersController expertsParametersController = loader.getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
