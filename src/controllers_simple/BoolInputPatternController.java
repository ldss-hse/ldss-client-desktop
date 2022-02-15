package controllers_simple;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;


public class BoolInputPatternController {
    @FXML
    private CheckBox fieldData;

    public Boolean getFieldData() {
        System.out.println(fieldData.isSelected());
        return fieldData.isSelected();
    }

    public void setParameters(String nameOfField) {
        this.fieldData.setText(nameOfField);
    }

    public void setWidthHeight(Double width, Double height) {
        this.fieldData.setPrefWidth(width);
        this.fieldData.setPrefHeight(height);
    }
}