/**
 * 
 */
package com.dbms.dbrp.controllers;

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
import javafx.scene.control.TextArea;
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
    
	int initVariables() throws SQLException, NoSuchAlgorithmException
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
	
	@FXML void uploadPaper(ActionEvent e){
		
		
		FileChooser fc = new FileChooser();
		fc.setTitle("Open paper");
		fc.showOpenDialog(new Stage());
	}
}
