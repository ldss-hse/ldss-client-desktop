package controllers_actions;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.grios.tableadapter.DefaultTableAdapter;

import controllers_simple.DateInputPatternController;
import controllers_simple.TextInputPatternController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

public class InsertAlternativesWindowController {
	int countColumns = 0, countRows = 0, curExpertId;
	String[] columns, columnsTypes;
	String[][] data;
	FXMLLoader[] columnsControllers;
	
	String url;
	Properties props;
	Connection conn;
	Statement st;
	ResultSet rs;
	
	String tableName;
	
    @FXML
    private Button insertButton;
    
	@FXML
	private FlowPane paneForElems;

	@FXML
	private TableView tableView;

	private DefaultTableAdapter dta;

	public void prepareTable(String expertId) throws Exception {
		curExpertId = Integer.valueOf(expertId);
		
		// Connection to DB
		readParametersOfDBConnection();

		// Read info about columns and their types
		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs = st.executeQuery("select column_name, data_type " 
										+ "from information_schema.columns "
										+ "where information_schema.columns.table_name='" + tableName + "';");
		rs.last();
		countColumns = rs.getRow();
		rs.beforeFirst();

		columns = new String[countColumns];
		columnsTypes = new String[countColumns];
		columnsControllers = new FXMLLoader[countColumns];
		int i = 0;
		
		FXMLLoader loader;
		Pane newPane;
		
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getString(2));

			columns[i] = rs.getString(1);
			columnsTypes[i] = rs.getString(2);
			i++;

			// Create a form for input of data into the column according to its type
			switch (rs.getString(2)) {
			case "integer":
			case "double precision":
			case "character varying":
		        loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("../patterns_simple/TextInputPattern.fxml"));

		        newPane = (Pane) loader.load();
		        columnsControllers[i - 1] = loader;
		        
		        paneForElems.getChildren().add(newPane);

		        TextInputPatternController textInputPatternController = loader.getController();
		        textInputPatternController.setParameters(rs.getString(1));
		        
		        if(rs.getString(1).equals("id_expert"))
		        	textInputPatternController.setFieldData(expertId);
		        
				break;
			case "date":
		        loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("../patterns_simple/DateInputPattern.fxml"));

		        newPane = (Pane) loader.load();
		        columnsControllers[i - 1] = loader;
		        
		        paneForElems.getChildren().add(newPane);

		        DateInputPatternController dateInputPatternController = loader.getController();
		        dateInputPatternController.setParameters(rs.getString(1));
				break;
			case "boolean":
		        loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("../patterns_simple/BoolInputPattern.fxml"));

		        newPane = (Pane) loader.load();
		        columnsControllers[i - 1] = loader;
		        
		        paneForElems.getChildren().add(newPane);

		        //DateInputPatternController dateInputPatternController = loader.getController();
		        //dateInputPatternController.setParameters(rs.getString(1));
				break;
			}
		}

		// Filling the created table with data
		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		rs = st.executeQuery("select * from " + tableName + " where id_expert = " + curExpertId + ";");
		rs.last();
		countRows = rs.getRow();
		rs.beforeFirst();

		data = new String[countRows > 0 ? countRows : 1][countColumns];
		for (int k = 0; k < countRows; k++) {
			for (int j = 0; j < countColumns; j++) {
				data[k][j] = "";
			}
		}
			
		i = 0;
		while (rs.next()) {
			for (int j = 0; j < countColumns; j++) {
				data[i][j] = rs.getString(j + 1);
				System.out.print(data[i][j] + " ");
			}
			i++;
			System.out.println();
		}

		dta = new DefaultTableAdapter(tableView, data, columns);

		rs.close();
		st.close();
	}
	
    @FXML
    void inputeDataFromGUI(ActionEvent event) throws Exception {
    	inputeDataFromGUI();
    }
	
	public void inputeDataFromGUI() throws Exception {
		String query = "insert into " + tableName + " values (";
		int i = 0;
		for(Node s : paneForElems.getChildren()) {
			switch (columnsTypes[i]) {
			case "integer":
			case "double precision":
			case "character varying":
		        TextInputPatternController textInputPatternController = columnsControllers[i].getController();
		        query += "'" + textInputPatternController.getFieldData() + "',";
				break;
			case "date":
		        DateInputPatternController dateInputPatternController = columnsControllers[i].getController();
		        query += "'" + dateInputPatternController.getFieldData() + "',";
				break;
			}
			i++;
		}
		
		query = query.substring(0, query.length() - 1) + ");";
		System.out.println(query);

		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		st.executeUpdate(query);
		conn.commit();
		
		// Filling the created table with data
		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs = st.executeQuery("select * from " + tableName + " where id_expert = " + curExpertId +  ";");
		rs.last();
		countRows = rs.getRow();
		rs.beforeFirst();

		data = new String[countRows][countColumns];
		i = 0;
		while (rs.next()) {
			for (int j = 0; j < countColumns; j++) {
				data[i][j] = rs.getString(j + 1);
				System.out.print(data[i][j] + " ");
			}
			i++;
			System.out.println();
		}

		dta = new DefaultTableAdapter(tableView, data, columns);

		rs.close();
		st.close();
	}
	

    @FXML
    void nextExpert(ActionEvent event) throws Exception {
    	curExpertId--;
    	
    	if(curExpertId > 0) {
    		paneForElems.getChildren().clear();
    	
    		prepareTable(String.valueOf(curExpertId));
    	} else {
    		int status = JOptionPane.showConfirmDialog(null,"Ввод завершен. Отправить данные для анализа?");
    		
    		//0 - Да, 1 - Нет, 2 - Cancel
    		if(status == 0) {
    			//Запаковка введенных альтератив по экспертам и отправка сервису на оценку
    			
    			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    			rs = st.executeQuery("select * from " + tableName + ";");

    			while (rs.next()) {
    				for (int j = 0; j < countColumns; j++)
    					System.out.print(rs.getString(j + 1) + " ");
    				System.out.println();
    			}

    			rs.close();
    			st.close();
    		}
    	}
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
            tableName = connectionProperties.getProperty("db.tablename");

            url = "jdbc:" + dbType + "://" + dbHost + "/" + dbName;
    		props = new Properties();
    		props.setProperty("user", dbUser);
    		props.setProperty("password", dbPassword);
    		conn = DriverManager.getConnection(url, props);
    		conn.setAutoCommit(false);
            
        } catch (IOException e) {
            System.err.println("ОШИБКА: Файл свойств отсуствует!");
        } catch (Exception e) {
			e.printStackTrace();
		}
    }
}