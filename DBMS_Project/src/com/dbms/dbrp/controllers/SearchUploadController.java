/**
 * 
 */
package com.dbms.dbrp.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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
	@FXML private Label uploadLabel;
	
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
	@FXML void uploadPaper(ActionEvent e) throws IOException{
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
		String t = title_u.getText();
		String[] aList = authors_u.getText().split(",");
		String[] cList = citations_u.getText().split("\n");
		String a = abstract_u.getText();
		
		initVariables();
		stmt.executeQuery("USE dbmsProject;");
		stmt.executeQuery("CREATE TABLE papers"
				+"(pid integer PRIMARY KEY, "
				+"title VARCHAR(400)"
				+ "rank integer);");
		stmt.executeQuery("CREATE TABLE authors"
				+"(aid integer PRIMARY KEY, "
				+"title VARCHAR(400)"
				+ "rank integer);");
		rs = stmt.executeQuery(sql);
		if(rs.next())
			count = rs.getInt(1);
		
	}
}
