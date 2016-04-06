/**
 * 
 */
package com.dbms.dbrp.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
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
		return flag;
	}
	
	@FXML void uploadPaper(ActionEvent e) throws IOException, SQLException
	{
		initVariables();
		FileChooser fc = new FileChooser();
		fc.setTitle("Open paper");
		int count = 1;
		File file = fc.showOpenDialog(null), cp = new File("data/" + count++ + ".pdf");
		if (file != null) {
			if (file.toString().endsWith("pdf")) {
				Files.copy(file.toPath(), cp.toPath());
			}
			else
			{
				uploadLabel.setText("Please choose a PDF file");
				uploadLabel.setTextFill(Color.RED);
			}
		}
	}
	
	@FXML void submit(ActionEvent e) throws SQLException{
		String title = title_u.getText();
		String[] authorList = authors_u.getText().split(",");
		String[] citationList = citations_u.getText().split("\n");
		String abstractText = abstract_u.getText();
		String conference = conference_u.getText();
		Boolean flag2 = (title.length()==0 || authorList.length == 0 || conference.length() == 0 || abstractText.length() == 0 ||  citationList.length == 0);
		initVariables();
		if(flag2 == true)
		{
			uploadLabel.setText("Enter all fields before submitting");
			uploadLabel.setTextFill(Color.RED);
		}
		else
		{
			uploadLabel.setText("");
			sql = "USE dbmsProject;";
			stmt.executeQuery(sql);
			rs = conn.getMetaData().getTables(null, null, "papers", null);
//			int id=1;
			if(!rs.next())
			{
				System.out.println("potato");
				sql = "CREATE TABLE papers "
						+"(tid INTEGER PRIMARY KEY, "
						+"title VARCHAR(400), "
						+"author VARCHAR(400), "
						+"keywords VARCHAR(400), "
						+"conference VARCHAR(400), "
						+"citationcount INTEGER, "
						+"citations VARCHAR(1000));"; 
				stmt.executeUpdate(sql);
				sql = "CREATE TABLE keywords "
						+"(tid INTEGER PRIMARY KEY, "
						+"word VARCHAR(40), "
						+"frequency INTEGER);";
				stmt.executeUpdate(sql);
				sql = "CREATE TABLE conference "
						+"(name VARCHAR(400) PRIMARY KEY, "
						+"date DATE PRIMARY KEY, "
						+"tid INTEGER);";
				stmt.executeUpdate(sql);
				sql = "CREATE TABLE author "
						+"(name VARCHAR(400) PRIMARY KEY, "
						+"affiliation VARCHAR(400), "
						+"address VARCHAR(400);";
				stmt.executeUpdate(sql);
			}
		}
		
	}
}
