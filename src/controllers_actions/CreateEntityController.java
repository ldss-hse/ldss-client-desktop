package controllers_actions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Properties;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class CreateEntityController {
	String url;
	Connection conn = null;
	Statement st = null;
	ResultSet rs = null;

	Properties props;
	
	ArrayList<FXMLLoader> entityAttributesControllers = new ArrayList<FXMLLoader>();

    @FXML
    private Button createEntityButton;

    @FXML
    private TextField entityName;

    @FXML
    private FlowPane attributesPane;

    @FXML
    private Button addAttributeButton;

    @FXML
    void addAttribute(ActionEvent event) throws Exception {
		FXMLLoader loader;
		Pane newPane;

		loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("../patterns_actions/entityAttribute.fxml"));

		entityAttributesControllers.add(loader);

		newPane = (Pane) loader.load();
		attributesPane.getChildren().add(newPane);

		entityAttributeController attributeController = loader.getController();
		attributeController.initAttributeTypes();
    }

    @FXML
    void createEntity(ActionEvent event) throws Exception {
    	String query = "create table ";
    	query += entityName.getText();
    	query += "(id_expert int, ";

    	for (int i = 0; i < attributesPane.getChildren().size(); i++) {
    		entityAttributeController attributeController = entityAttributesControllers.get(i).getController();

    		String[] entityAttributesData = attributeController.getAttributeMetaData();

    		if (i == attributesPane.getChildren().size() - 1)
    			query += entityAttributesData[0] + " " + entityAttributesData[1];
    		else
    			query += entityAttributesData[0] + " " + entityAttributesData[1] + ", ";
    	}

    	query += ");";

    	System.out.println(query);
    	setTableNameProperty(entityName.getText());
    	
    	createEntityInBD(query);
    }

    void createEntityInBD(String query) throws Exception {
    	readParametersOfDBConnection();
    	
		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

		st.executeUpdate(query);
    }
    
    public void readParametersOfDBConnection() {
    	FileInputStream fis;
    	Properties connectionProperties = new Properties();
    	
        try {
            fis = new FileInputStream("src/properties/config.properties");
            connectionProperties.load(fis);

            String	dbType = connectionProperties.getProperty("db.type"),
            		dbHost = connectionProperties.getProperty("db.host"),
            		dbName = connectionProperties.getProperty("db.name"),
            		dbUser = connectionProperties.getProperty("db.user"),
            		dbPassword = connectionProperties.getProperty("db.password");
            
            fis.close();

            url = "jdbc:" + dbType + "://" + dbHost + "/" + dbName;
    		props = new Properties();
    		props.setProperty("user", dbUser);
    		props.setProperty("password", dbPassword);
    		conn = DriverManager.getConnection(url, props);
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void setTableNameProperty(String tableName) {
    	FileInputStream fis;
    	Properties connectionProperties = new Properties();
    	
        try {
            fis = new FileInputStream("src/properties/config.properties");
            connectionProperties.load(fis);
            
            connectionProperties.setProperty("db.tablename", tableName);
            connectionProperties.store(new FileOutputStream("src/properties/config.properties"),null); 
            
            fis.close();
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
}