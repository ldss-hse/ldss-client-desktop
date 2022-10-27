package controllers_simple;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.text.Text;

public class DateInputPatternController {

    @FXML
    private DatePicker fieldData;

    @FXML
    private Text nameOfField;
    
    public String getFieldData() {
    	System.out.println(fieldData.getValue());
    	return fieldData.getValue().toString();
    }
    
    public void setParameters(String nameOfField) {
    	this.nameOfField.setText(nameOfField);
    }

}
