/**
 * 
 */
package com.dbms.dbrp.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;
import com.dbms.dbrp.utilities.IDGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author ambar14012, harshvardhan14043
 *
 */
public class SearchUploadController {
	@FXML private TextArea title_u;
	@FXML private TextArea authors_u;
	@FXML private TextArea abstract_u;
	@FXML private TextArea citations_u;
	@FXML private TextArea conference_u;
	@FXML private TextField date_u;
	@FXML private Label uploadLabel;
	@FXML private Button submit,uploadPaper;
	
	Connection conn;
	Statement stmt;
	ResultSet rs;
	int flag, count;
	String sql, dbname, usr, pwd;
	MessageDigest md;
	byte byteData[];
	StringBuffer sb;
	Node source; 
    Stage stage;
    Boolean isUploaded;
    
    public SearchUploadController() throws SQLException{
    	initVariables();
	}
	int initVariables() throws SQLException
	{
		conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getCatalogs();
		while(rs.next())
		{
			dbname = rs.getString(1).trim();
			if(dbname.equalsIgnoreCase("dbmsProject"))
				flag = 1;
		}
		isUploaded = false;
		return flag;
	}
	
	void fixCitations(){
		// Fix all citations
	}
	int keyWordExists(String keyword){
		// return kid if keyWord exists in the Database else -1
		return -1;
	}
	int conferenceExists(String conference, String date) throws SQLException{
		// return cid id conference exists in the Database else -1
		sql = "select CID from conference where name = " + conference + " and date = " + date + ";";
		rs = stmt.executeQuery(sql);
		if(rs.next())
			return rs.getInt("CID");
		return -1;
	}
	@FXML void uploadPaper(ActionEvent e) throws IOException, SQLException
	{
    	title_u.setText(":)");
    	authors_u.setText("::)");
    	abstract_u.setText("::))");
    	citations_u.setText(":/");
    	conference_u.setText(":'(");
		FileChooser fc = new FileChooser();
		fc.setTitle("Open paper");
		BufferedReader b = new BufferedReader(new FileReader("data/counter"));
		String temp = b.readLine().toString();
		int count = Integer.parseInt(temp);
		b.close();
		File file = fc.showOpenDialog(null), cp = new File("data/" + count++ + ".pdf");
		if (file != null) {
			if (file.toString().endsWith("pdf")) {
				Files.copy(file.toPath(), cp.toPath());
				isUploaded = true;
				System.out.println(isUploaded);
				uploadLabel.setText("PDF uploaded");
				uploadLabel.setTextFill(Color.GREEN);
			}
			else
			{
				uploadLabel.setText("Please choose a PDF file");
				uploadLabel.setTextFill(Color.RED);
			}
		}
		System.out.println(isUploaded);
		BufferedWriter br = new BufferedWriter(new FileWriter("data/counter"));
		br.write(count+"");
		br.close();
	}
	
	@FXML void submit(ActionEvent e) throws SQLException{
		System.out.println(isUploaded);
		String title = title_u.getText();
		String[] authorList = authors_u.getText().split(",");
		String[] citationList = citations_u.getText().split("\n");
		String abstractText = abstract_u.getText();
		String conference = conference_u.getText();
		String date = date_u.getText();
		Boolean flag2 = (title.length()==0 || authorList.length == 0 || conference.length() == 0 || abstractText.length() == 0 ||  citationList.length == 0);
		if(flag2 == true)
		{
			uploadLabel.setText("Enter all fields before submitting");
			uploadLabel.setTextFill(Color.RED);
		}
		else if (isUploaded.booleanValue() == false){
			uploadLabel.setText("You need to submit a pdf");
			uploadLabel.setTextFill(Color.RED);	
		}
		else
		{
			uploadLabel.setText("");
			sql = "USE dbmsProject;";
			stmt.executeQuery(sql);
			rs = conn.getMetaData().getTables(null, null, "papers", null);
			if(!rs.next())
			{
				// Tables
				sql = "CREATE TABLE papers "
						+"(pid INTEGER PRIMARY KEY, "
						+"title VARCHAR(400), "
						+"citationcount INTEGER);"; 
				stmt.executeUpdate(sql);
				
				sql = "CREATE TABLE keyword "
						+"(kid INTEGER PRIMARY KEY, "
						+"word VARCHAR(40));";
				stmt.executeUpdate(sql);
				
				sql = "CREATE TABLE conference "
						+"(cid INTEGER PRIMARY KEY, "
						+"name VARCHAR(400), "
						+"date DATE);";
				stmt.executeUpdate(sql);
				sql = "CREATE TABLE author "
						+"(aid INTEGER PRIMARY KEY, "
						+"name VARCHAR(400), "
						+"affiliation VARCHAR(400));";
				stmt.executeUpdate(sql);
				
				// Relationships
				sql = "CREATE TABLE isAuthorOf "
						+"(pid INTEGER, "
						+"aid INTEGER);"; 
				stmt.executeUpdate(sql);
				
				sql = "CREATE TABLE isCitationOf "
						+"(pid INTEGER, "
						+"pid INTEGER);"; 
				stmt.executeUpdate(sql);	

				sql = "CREATE TABLE isPublishedin "
						+"(pid INTEGER, "
						+"cid INTEGER);"; 
				stmt.executeUpdate(sql);
				
				sql = "CREATE TABLE isKeywordIn"
						+"(kid INTEGER, "
						+"pid INTEGER);";
				stmt.executeQuery(sql);
			}
			
			// Insert Paper into Paper Table
			sql = "INSERT into paper values( " + IDGenerator.getPaperCounter()
					+ "," + title
					+ "," + 0
					+ ");";
			fixCitations();
			stmt.executeQuery(sql);
			
			// Insert keywords into table
			for (String keyWord : abstractText.split(" ")){
				int id = keyWordExists(keyWord);
				if (id == -1){
					sql = "INSERT into keyword values( " + IDGenerator.getKeywordCounter()
						  + "," + keyWord + ");";
					stmt.executeQuery(sql);
				}
			}
			
			// Insert into conference table
			sql = "INSERT into conference values( " + IDGenerator.getConferenceCounter()
			+ " , " + conference + " , " + date + ");";
			stmt.executeQuery(sql);
		}
		
	}
}
