package controllers_actions;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class entityAttributeController {
    @FXML
    private ChoiceBox attributeType;

    @FXML
    private TextField attributeName;

    public void initAttributeTypes() {
    	//select * from INFORMATION_SCHEMA.columns where TABLE_NAME=N'LstAnaliz'
    	attributeType.getItems().addAll("", "int", "date", "float", "varchar");
    }

    public String[] getAttributeMetaData() {
    	String[] attributeMetaData = new String[2];

    	attributeMetaData[0] = attributeName.getText();
    	attributeMetaData[1] = attributeType.getSelectionModel().getSelectedItem().toString();

    	return attributeMetaData;
    }
}
