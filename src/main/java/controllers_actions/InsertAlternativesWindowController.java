package controllers_actions;

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
import okhttp3.*;
import org.grios.tableadapter.DefaultTableAdapter;

import javax.swing.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;



public class InsertAlternativesWindowController {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();
	
	int countColumns = 0, countRows = 0, curExpertId, expertCount;
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
		System.out.println("!!!");
		expertCount = curExpertId = Integer.valueOf(expertId);
		
		// Connection to DB
		readParametersOfDBConnection();

		// Read info about columns and their types
		st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		rs = st.executeQuery("select column_name, data_type " 
										+ "from information_schema.columns "
										+ "where information_schema.columns.table_name='" + tableName + "';");
		rs.last();
		countColumns = rs.getRow();
		System.out.println(countColumns + tableName + "\n" + "select column_name, data_type " 
				+ "from information_schema.columns "
				+ "where information_schema.columns.table_name='" + tableName + "';");
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
		        loader.setLocation(getClass().getResource("/controllers_simple/TextInputPattern.fxml"));

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
		        loader.setLocation(getClass().getResource("/controllers_simple/DateInputPattern.fxml"));

		        newPane = (Pane) loader.load();
		        columnsControllers[i - 1] = loader;
		        
		        paneForElems.getChildren().add(newPane);

		        DateInputPatternController dateInputPatternController = loader.getController();
		        dateInputPatternController.setParameters(rs.getString(1));
				break;
			case "boolean":
		        loader = new FXMLLoader();
		        loader.setLocation(getClass().getResource("/controllers_simple/BoolInputPattern.fxml"));

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
    			//System.out.println("ДЛина данных" + data.length);
    			//Запаковка введенных альтератив по экспертам и отправка сервису на оценку
    			prepareExpertJSON();
    			
    			sendJSON();

    			/*
    			st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    			rs = st.executeQuery("select * from " + tableName + ";");

    			while (rs.next()) {
    				for (int j = 0; j < countColumns; j++)
    					System.out.print(rs.getString(j + 1) + " ");
    				System.out.println();
    			}

    			rs.close();
    			st.close();*/
    		}
    	}
    }
    
    public void prepareExpertJSON() throws Exception {    	
    	FileWriter writer = new FileWriter(getTempJSONFileForRequest(), false);
        String text = "";
        
        //Ввод главного блока
        text = "{\r\n"
        		+ "   \"task_description\": \r\n"
        		+ "   {\r\n"
        		+ "		\"criteria\": {\r\n"
        		+ "			\"group1\": [\n";
        writer.write(text);
        
        //Ввод критериев
        for(int i = 0; i < columns.length; i++) {
        	if (i < columns.length - 1)
        		text = "{\r\n"
        				+ "				\"criteriaID\": \"" + columns[i] + "\",\r\n"
        				+ "				\"criteriaName\": \"" + columns[i] + " of Group 1\",\r\n"
        				+ "				\"qualitative\": false\r\n"
        				+ "				},\n";	
        	else
        		text = "{\r\n"
        				+ "				\"criteriaID\": \"" + columns[i] + "\",\r\n"
        				+ "				\"criteriaName\": \"" + columns[i] + " of Group 1\",\r\n"
        				+ "				\"qualitative\": false\r\n"
        				+ "				}\n";	
        	
        	writer.write(text);
        }
        text = "]\r\n"
        		+ "		},\n";
        writer.write(text);

        text = "		\"alternatives\": [\n";
        writer.write(text);
        
        //Ввод альтернатив
        for(int i = 0; i < data.length; i++) {
        	if (i < data.length - 1)
        		text = "			{\r\n"
        				+ "			\"alternativeID\": \"a" + (i + 1) + ".g1\",\r\n"
        				+ "			\"alternativeName\": \"Alternative " + (i + 1) +" from Group 1\",\r\n"
        				+ "			\"abstractionLevelID\": \"group1\"\r\n"
        				+ "			},\n";
        	else
        		text = "			{\r\n"
        				+ "			\"alternativeID\": \"a" + (i + 1) + ".g1\",\r\n"
        				+ "			\"alternativeName\": \"Alternative " + (i + 1) +" from Group 1\",\r\n"
        				+ "			\"abstractionLevelID\": \"group1\"\r\n"
        				+ "			}\n";
        	
        	writer.write(text);
        }
        
        text = "		],\r\n"
        		+ "		\r\n"
        		+ "		\"scales\": [{\r\n"
        		+ "        \"scaleID\": \"Scale_Seven\",\r\n"
        		+ "        \"scaleName\": \"Scale Seven Name\",\r\n"
        		+ "        \"labels\": [\r\n"
        		+ "          \"vp\",\r\n"
        		+ "          \"p\",\r\n"
        		+ "          \"mp\",\r\n"
        		+ "          \"m\",\r\n"
        		+ "          \"mg\",\r\n"
        		+ "          \"g\",\r\n"
        		+ "          \"vg\"\r\n"
        		+ "        ]\r\n"
        		+ "      }],\r\n\n";
        writer.write(text);
        
        text = "		\"abstractionLevels\": [\r\n"
        		+ "			{\r\n"
        		+ "			\"abstractionLevelID\": \"group1\",\r\n"
        		+ "			\"abstractionLevelName\": \"Group1\"\r\n"
        		+ "			}\r\n"
        		+ "		],\n";
        writer.write(text);
        
        text = "		\"abstractionLevelWeights\": {\r\n"
        		+ "			\"1\": 1\r\n"
        		+ "		},\n";
        writer.write(text);
        
        text = "		\"expertWeightsRule\": {\n";
        writer.write(text);
        //Веса экспертов
        for (int i = 1; i < expertCount; i++) {
        	text = "			\"" + i + "\": " + (1/expertCount) + ",\n";
        	writer.write(text);
        }
    	text = "			\"" + expertCount + "\": " + (1/expertCount) + "\n 		},\n";
    	writer.write(text);
    	
    	//Эксперты
    	text = "		\"experts\": [\n";
    	writer.write(text);
        for (int i = 1; i < expertCount; i++) {
        	text = "			{\r\n"
        			+ "			\"expertName\": \"expert" + i + "\",\r\n"
        			+ "			\"expertID\": \"expert" + i + "\",\r\n"
        			+ "			\"competencies\": [\r\n"
        			+ "				\"competence1\"\r\n"
        			+ "			]\r\n"
        			+ "			},\n";
        	writer.write(text);
        }
    	text = "			{\r\n"
    			+ "			\"expertName\": \"expert" + expertCount + "\",\r\n"
    			+ "			\"expertID\": \"expert" + expertCount + "\",\r\n"
    			+ "			\"competencies\": [\r\n"
    			+ "				\"competence1\"\r\n"
    			+ "			]\r\n"
    			+ "			}\n";
    	writer.write(text);
    	
    	text = "		],\n";
    	writer.write(text);
    	
    	text = "		\"estimations\": {\n";
    	writer.write(text);
    	
    	//Ввод оценок критериев по альтернативам
		
    	for (int i = 1; i <= expertCount; i++) {
        	st = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    		rs = st.executeQuery("select * from " + tableName + " where id_expert = " + i + ";");
    		
    		text = "			\"expert" + i + "\": [\n";
    		writer.write(text);
    		
    		int curAlternative = 1;
    		
    		while (rs.next()) {
    			text = "				{\r\n"
    					+ "				\"alternativeID\": \"a" + curAlternative + ".g1\",";
    			writer.write(text);
    			
    			text = "				\"criteria2Estimation\": [\n";
    			writer.write(text);
    			
    			for (int j = 0; j < countColumns; j++) {
    				text = "					{\r\n"
    						+ "					\"criteriaID\": \"" + columns[j] + "\",\n";
    				writer.write(text);
    			
    				if (j < countColumns - 1)
		    			text = "					\"estimation\": [\r\n"
		    					+ "						\"" + rs.getString(j + 1) + "\"\r\n"
		    					+ "					],\r\n"
		    					+ "					\"qualitative\": false\r\n"
		    					+ "					},\n";
    				else
		    			text = "					\"estimation\": [\r\n"
		    					+ "						\"" + rs.getString(j + 1) + "\"\r\n"
		    					+ "					],\r\n"
		    					+ "					\"qualitative\": false\r\n"
		    					+ "					}\n";
    				writer.write(text);

    			//System.out.println();	


    			}
    			
    			text = "]\n}";
    			writer.write(text);
    			
    			if(curAlternative == data.length)
        			text = "\n";
    			else
    				text = ",\n";
        		writer.write(text);
        		curAlternative++;
    		}
    		
			if(i < expertCount)
				text = "],\n}";
			else
				text = "]\n}";
			writer.write(text);
    	}

		rs.close();
		st.close();
    	

    	text = "}\n}";
    	writer.write(text);
        writer.flush();
        
        writer.close();
    }
    
    public void sendJSON() throws Exception {
        String json = bowlingJson();
        System.out.println(json);
        
        String response = post("https://ldss-core-api-app.herokuapp.com/api/v1/make-decision", json);
        System.out.println(response);
        String[] responseResult = parseResponse(response);
        String result = "Рейтинг альтернатив: \n";
        
        for (int i = 0; i < responseResult.length; i++) {
        	result += (i + 1) + "." + responseResult[i] + "\n";
        }
        
        JOptionPane.showMessageDialog(null, result);
    }
    
    String[] parseResponse(String response) {
    	int orderedAlternativesStart = response.indexOf("\"alternativesOrdered\":[") + 23,
    		orderedAlternativesEnd = response.indexOf("],\"expertWeightsRule\"");
    	
    	String orderedAlternatives = response.substring(orderedAlternativesStart, orderedAlternativesEnd);
    	
    	System.out.println("\n" + response.substring(orderedAlternativesStart, orderedAlternativesEnd));
    	
    	int countOfOrderedAlternatives = orderedAlternatives.split("\"alternativeID\":\"").length - 1;
    	
    	String[] orderedAlternativesArray = new String[countOfOrderedAlternatives];
    	
    	int indexStart = orderedAlternatives.indexOf("\"alternativeID\":\"") + 17,
    		indexEnds = orderedAlternatives.indexOf("\",\"estimation\"");
    	
    	while (countOfOrderedAlternatives > 0 ) {
    		orderedAlternativesArray[orderedAlternativesArray.length - countOfOrderedAlternatives] = orderedAlternatives.substring(indexStart, indexEnds);

    		countOfOrderedAlternatives--;
    		
    		indexStart = orderedAlternatives.indexOf("\"alternativeID\":\"", indexEnds) + 17;
    	    indexEnds = orderedAlternatives.indexOf("\",\"estimation\"", indexStart);
    	}
    	
    	for (String str : orderedAlternativesArray)
    		System.out.println(str);
    	
    	return orderedAlternativesArray;
    }
    
    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder().url(url).post(body).build();

		Call call = client.newCall(request);
		Response response = call.execute();
        
		final String responseAsText = response.body().string();
		return responseAsText;
    }

    String bowlingJson() throws Exception {
    	BufferedReader reader = new BufferedReader(new FileReader (getTempJSONFileForRequest()));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }
		reader.close();
        
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
    
    private File getTempJSONFileForRequest() {
		File tempDirectory = new File(System.getProperty("java.io.tmpdir"));
		File newFile = new File(tempDirectory.getAbsolutePath() + File.separator + "testJSON.json");
		System.out.println("Creating a JSON file in: " + newFile);
		return newFile;
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