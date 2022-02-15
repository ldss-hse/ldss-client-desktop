package controllers_simple;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import controllers_simple.MaskField;

public class PhoneMaskInputPatternController {
	String fieldOriginalName;
	
    @FXML
    public FlowPane flowPane;

    @FXML
    private MaskField fieldData;

    @FXML
    private Text nameOfField;

    public String getFieldData() {
        if (checkData() != 0)
            return "";
        else {
            System.out.println(fieldData.getText());
            return fieldData.getText();
        }
    }
    
    public void setFieldData(String data) {
    	fieldData.setText(data);
    }

    public void setParameters(String originalNameOfField, String nameOfField) {
    	this.fieldOriginalName = originalNameOfField;
    	this.nameOfField.setText(nameOfField);
    }

    public void setWidthHeight(Double paneWidth, Double paneHeight, Double labelWidth) {
        this.flowPane.setPrefWidth(paneWidth);
        this.flowPane.setPrefHeight(paneHeight);

        this.nameOfField.setWrappingWidth(labelWidth);
        this.fieldData.setPrefWidth(paneWidth - labelWidth - 40.0);
        this.fieldData.setPrefHeight(paneHeight*0.714);
    }

    public void setEditable(Boolean editChoice) {
        this.fieldData.setDisable(!editChoice);
    }
    
     public int checkData(){
         return 0;
     }
}
