/*package controllers_simple;

import application.MainWindowController;
import controllers_tabs.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import application.InsertFormController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class AddEditDeleteButtonsController {
	private String tabName;
	private String[] fields, fieldsTypes;
	private FXMLLoader[] fieldsControllers;
	FXMLLoader tabController;

	@FXML
	private HBox buttonsBox;

    @FXML
    public Button addButton;

    @FXML
    public Button editButton;

    @FXML
    public Button deleteButton;

    @FXML
    void addButtonAction(ActionEvent event) throws Exception {
        switch (tabName){
            case "Вступительные испытания":
                EntranceExamTabController entranceExamTabController = tabController.getController();
                fieldsControllers = entranceExamTabController.addRow();
                break;
			case "Конкурсные группы":
				CompetitiveGroupsTabController competitiveGroupsTabController = tabController.getController();
				fieldsControllers = competitiveGroupsTabController.addRow();
				break;
            case "Индивидуальные достижения":
                IndividualAchievementsTabController individualAchievementsTabController = tabController.getController();
                fieldsControllers = individualAchievementsTabController.addRow();
                break;
            case "Доп. сведения":
            	AdditionalInfoTabController additionalInfoTabController = tabController.getController();
                fieldsControllers = additionalInfoTabController.addRow();
                break;
			case "100б":
				OlympiadsTabController olympiadsTabController = tabController.getController();
				fieldsControllers = olympiadsTabController.addRow();
				break;
			case "Привилегии":
				PrivilegeTabController privilegeTabController = tabController.getController();
				fieldsControllers = privilegeTabController.addRow();
				break;
		}
    }


    @FXML
    void editButtonAction(ActionEvent event) throws IOException, SQLException {
    	Boolean activate;

    	/*if (tabName.equals("Конкурсные группы")) {
            CompetitiveGroupsTabController competitiveGroupsTabController = tabController.getController();
            competitiveGroupsTabController.openModalWindow();
            return;
        }

    	switch(editButton.getText()) {
			case "Редактировать":
				editButton.setText("Сохранить");
				//each element have to be activated to editable mode
				activate = true;
				this.setEditable(activate);
				break;
			case "Сохранить":
				try {
					if (checkData() == 0) {
						//each element have to be activated to non-editable mode
						editButton.setText("Редактировать");
						activate = false;

						String[] fieldsData = getFieldsData();

						/*for(String curFieldsData : fieldsData)
							System.out.println(curFieldsData);

						//Выбор нужной операции передачи данных в БД в зависимости от вкладки
				    	switch (tabName) {
							case "АРМ по приему в ВУЗ":
								MainWindowController mainWindowController = tabController.getController();
								mainWindowController.uploadFieldsDataToDataBase(fieldsData);
								break;
					    	case "SampleTab":
					    		InsertFormController insertFormController = tabController.getController();
					    		insertFormController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
					    	case "Паспорт и ИНН":
					            PassportTabController passportTabController = tabController.getController();
					    		passportTabController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
					    	case "Образование":
					            EducationTabController educationTabController = tabController.getController();
					            educationTabController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
					    	case "Вступительные испытания":
					    		EntranceExamTabController entranceExamTabController = tabController.getController();
					    		entranceExamTabController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
					    	case "Доп. сведения":
					    		AdditionalInfoTabController additionalInfoTabController = tabController.getController();
					    		additionalInfoTabController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
							case "100б":
								OlympiadsTabController olympiadsTabController = tabController.getController();
								olympiadsTabController.uploadFieldsDataToDataBase(fieldsData);
								break;
					    	case "Индивидуальные достижения":
					    		IndividualAchievementsTabController individualAchievementsTabController = tabController.getController();
					    		individualAchievementsTabController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
					    	case "Конкурсные группы":
					    		CompetitiveGroupsTabController competitiveGroupsTabController = tabController.getController();
					    		competitiveGroupsTabController.uploadFieldsDataToDataBase(fieldsData);
					    		break;
                            case "Привилегии":
                                PrivilegeTabController privilegeTabController = tabController.getController();
                                privilegeTabController.uploadFieldsDataToDataBase(fieldsData);
                                break;
				    	}

		    			this.setEditable(activate);
					}
	    			break;
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
    	}
    }


    public String[] getFieldsData() {
    	if (fieldsControllers == null) return null;

    	String[] fieldsData = new String[fieldsControllers.length];

    	for (int i = 0,j = 0; i < (fieldsControllers == null ? 0 : fieldsControllers.length); i++,j++) {
			if(j==fieldsTypes.length)
				j=0;
			switch (fieldsTypes[j]) {
				case "date":
					DateInputPatternController dateInputPatternController = fieldsControllers[i].getController();
					fieldsData[i] = dateInputPatternController.getFieldData();
					if (fieldsData[i].equals("")) fieldsData[i] = "null";
					break;
				case "double":
					DoubleInputPatternController doubleInputPatternController = fieldsControllers[i].getController();
					fieldsData[i] = doubleInputPatternController.getFieldData();
					if (fieldsData[i].equals("")) fieldsData[i] = "null";
					break;
				case "int":
					if(Pattern.compile("(id_).*").matcher(fields[j]).matches() ){
						ChoiceInputPatternController choiceInputPatternController = fieldsControllers[i].getController();
						fieldsData[i] = choiceInputPatternController.getFieldData();
						if (fieldsData[i].equals("0")) fieldsData[i] = "null";
						break;
					}
					if(Pattern.compile("(need).*").matcher(fields[j]).matches()
							|| Pattern.compile("(ha).*").matcher(fields[j]).matches()
							|| Pattern.compile("(is).*").matcher(fields[j]).matches()){
						BoolInputPatternController boolInputPatternController = fieldsControllers[i].getController();
						fieldsData[i] = boolInputPatternController.getFieldData();
						break;
					}
					/* If we don't need "is_enrolled" change select or:
                    if(Pattern.compile("(is_).*").matcher(fields[i]).matches())
                        break;
					else{
						IntInputPatternController intInputPatternController = fieldsControllers[i].getController();
						fieldsData[i] = intInputPatternController.getFieldData();
						if (fieldsData[i].equals("")) fieldsData[i] = "null";
						break;
					}
				case "varchar":
					if(Pattern.compile("(phone).*").matcher(fields[j]).matches()){
						PhoneMaskInputPatternController phoneMaskInputPatternController = fieldsControllers[i].getController();
						fieldsData[i] = phoneMaskInputPatternController.getFieldData();
						if (fieldsData[i].equals("")) fieldsData[i] = "null";
						break;
					}
					if(Pattern.compile("(passw).*").matcher(fields[j]).matches()){
						PasswordPatternController passwordInputPatternController = fieldsControllers[i].getController();
						fieldsData[i] = passwordInputPatternController.getFieldData();
						if (fieldsData[i].equals("")) fieldsData[i] = "null";
						break;
					}
					else {
						TextInputPatternController textInputPatternController = fieldsControllers[i].getController();
						fieldsData[i] = textInputPatternController.getFieldData();
						if (fieldsData[i].equals("")) fieldsData[i] = "null";
						break;
					}
			}
			fieldsData[i] = "'" + fieldsData[i] + "'";
		}
    	return fieldsData;
    }


    @FXML
    void deleteButtonAction(ActionEvent event) throws Exception {
        switch (tabName){
            case "Вступительные испытания":
                EntranceExamTabController entranceExamTabController = tabController.getController();
                fieldsControllers = entranceExamTabController.deleteRow();
                break;
            case "Доп. сведения":
				AdditionalInfoTabController additionalInfoTabController = tabController.getController();
				fieldsControllers = additionalInfoTabController.deleteRow();
				break;
			case "100б":
				OlympiadsTabController olympiadsTabController = tabController.getController();
				fieldsControllers = olympiadsTabController.deleteRow();
				break;
			case "Индивидуальные достижения":
				IndividualAchievementsTabController individualAchievementsTabController = tabController.getController();
				fieldsControllers = individualAchievementsTabController.deleteRow();
				break;
	    	case "Конкурсные группы":
	    		CompetitiveGroupsTabController competitiveGroupsTabController = tabController.getController();
	    		fieldsControllers = competitiveGroupsTabController.deleteRow();
	    		break;
			case "Привилегии":
				PrivilegeTabController privilegeTabController = tabController.getController();
				fieldsControllers = privilegeTabController.deleteRow();
				break;
        }
    }


    public void setParameters(String tabName, FXMLLoader tabController, String[] fields, String[] fieldsTypes, FXMLLoader[] fieldsControllers) {
        this.tabName = tabName;
        this.tabController = tabController;
    	this.fields = fields.clone();
        this.fieldsTypes = fieldsTypes.clone();
        this.fieldsControllers = fieldsControllers.clone();

       // setEditable(false);

        //Here will be switch/case according to the tabName (on some AddButton/DeleteButton have to be hidden)
    }


    public void setWidthHeight(Double width, Double height) {
        buttonsBox.setPrefWidth(width);
        buttonsBox.setPrefHeight(height);

        this.addButton.setPrefWidth(width*0.3);
        this.addButton.setPrefHeight(height*0.35);
		this.editButton.setPrefWidth(width*0.33);
		this.editButton.setPrefHeight(height*0.35);
		this.deleteButton.setPrefWidth(width*0.3);
		this.deleteButton.setPrefHeight(height*0.35);
    }


	public void setWidthHideButtons(Double width, Double height, Integer visibleButtons) {
		buttonsBox.setPrefWidth(width);
		buttonsBox.setPrefHeight(height);

		this.addButton.setPrefHeight(height*0.35);
		this.editButton.setPrefHeight(height*0.35);
		this.deleteButton.setPrefHeight(height*0.35);

		if (visibleButtons == 3) {
			this.addButton.setPrefWidth(width*0.3);
			this.editButton.setPrefWidth(width*0.33);
			this.deleteButton.setPrefWidth(width*0.3);
		} else if (visibleButtons == 2) {
			this.addButton.setPrefWidth(width*0.45);
			this.editButton.setPrefWidth(width*0.45);
			this.deleteButton.setPrefWidth(width*0.45);
		} else if (visibleButtons == 1) {
			this.addButton.setPrefWidth(width*0.85);
			this.editButton.setPrefWidth(width*0.85);
			this.deleteButton.setPrefWidth(width*0.85);
		}
	}


    public void hideButton(int numberOfButton) {
    	this.addButton.setVisible(numberOfButton == 0 ? false : true);
    	this.editButton.setVisible(numberOfButton == 1 ? false : true);
    	this.deleteButton.setVisible(numberOfButton == 2 ? false : true);

		//this.buttonsBox.getChildren().remove(numberOfButton);
	}


	public void hideButton2(int numberOfButton) {
		this.buttonsBox.getChildren().remove(numberOfButton);
	}


    public void setEditable(Boolean value) {
    	this.addButton.setDisable(!value);
    	this.deleteButton.setDisable(!value);

		for (int i = 0,j = 0; i < (fieldsControllers == null ? 0 : fieldsControllers.length); i++,j++) {
            if(j==fieldsTypes.length)
                j=0;
			switch (fieldsTypes[j]) {
				case "date":
					DateInputPatternController dateInputPatternController = fieldsControllers[i].getController();
					dateInputPatternController.setEditable(value);
					break;
				case "double":
					DoubleInputPatternController doubleInputPatternController = fieldsControllers[i].getController();
					doubleInputPatternController.setEditable(value);
					break;
				case "int":
					if (Pattern.compile("(id_).*").matcher(fields[j]).matches() ){
						ChoiceInputPatternController choiceInputPatternController = fieldsControllers[i].getController();
						choiceInputPatternController.setEditable(value);
						break;
					}
					if (Pattern.compile("(need).*").matcher(fields[j]).matches()
							|| Pattern.compile("(ha).*").matcher(fields[j]).matches()
							|| Pattern.compile("(is).*").matcher(fields[j]).matches()) {
						BoolInputPatternController boolInputPatternController = fieldsControllers[i].getController();
						boolInputPatternController.setEditable(value);
						break;
					} else {
						IntInputPatternController intInputPatternController = fieldsControllers[i].getController();
						intInputPatternController.setEditable(value);
						break;
					}
				case "varchar":
					if(Pattern.compile("(phone).*").matcher(fields[j]).matches()){
						PhoneMaskInputPatternController phoneMaskInputPatternController = fieldsControllers[i].getController();
						phoneMaskInputPatternController.setEditable(value);
						break;
					}
					if(Pattern.compile("(passw).*").matcher(fields[j]).matches()){
						PasswordPatternController passwordInputPatternController = fieldsControllers[i].getController();
						passwordInputPatternController.setEditable(value);
						break;
					}
					else {
						TextInputPatternController textInputPatternController = fieldsControllers[i].getController();
						textInputPatternController.setEditable(value);
						break;
					}
			}
		}
    }


    public int checkData() {
    	// Проверка в зависимости от вкладки
    	switch (tabName) {
			case "АРМ по приему в ВУЗ":
				MainWindowController mainWindowController = tabController.getController();
				return mainWindowController.checkData();
	    	case "SampleTab":
	    		InsertFormController insertFormController = tabController.getController();
	    		return insertFormController.checkData();
	    	case "Паспорт и ИНН":
	            PassportTabController passportTabController = tabController.getController();
	    		return passportTabController.checkData();
	    	case "Образование":
	            EducationTabController educationTabController = tabController.getController();
	    		return educationTabController.checkData();
	    	case "Вступительные испытания":
	    		EntranceExamTabController entranceExamTabController = tabController.getController();
	    		return entranceExamTabController.checkData();
			case "Доп. сведения":
				AdditionalInfoTabController additionalInfoTabController = tabController.getController();
				return additionalInfoTabController.checkData();
			case "100б":
				OlympiadsTabController olympiadsTabController = tabController.getController();
				return olympiadsTabController.checkData();
			case "Индивидуальные достижения":
				IndividualAchievementsTabController individualAchievementsTabController = tabController.getController();
				return individualAchievementsTabController.checkData();
			case "Конкурсные группы":
				CompetitiveGroupsTabController competitiveGroupsTabController = tabController.getController();
				return competitiveGroupsTabController.checkData();
			case "Привилегии":
				PrivilegeTabController privilegeTabController = tabController.getController();
				return privilegeTabController.checkData();
	    	default:
	    		return 0;
    	}
    }
}*/