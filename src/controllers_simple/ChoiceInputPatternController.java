package controllers_simple;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class ChoiceInputPatternController {

    @FXML
    private ChoiceBox<?> fieldData;

    @FXML
    private Text nameOfField;

    public String getFieldData() {
        System.out.println(fieldData.getSelectionModel().getSelectedItem());
        return fieldData.getSelectionModel().getSelectedItem().toString();
    }

    public void setParameters(String nameOfField) {
        this.nameOfField.setText(nameOfField);
    }

}
