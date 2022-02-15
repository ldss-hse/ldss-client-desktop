package controllers_simple;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class DoubleInputPatternController {

    @FXML
    private TextField fieldData;

    @FXML
    private Text nameOfField;

    public String getFieldData() {
        System.out.println(fieldData.getText());
        return fieldData.getText();
    }

    public void setParameters(String nameOfField) {
        this.nameOfField.setText(nameOfField);
    }

}
