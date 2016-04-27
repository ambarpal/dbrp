package com.dbms.dbrp.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;
import com.dbms.dbrp.utilities.IDGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class AuthorController {
	@FXML TextField author_name;
	@FXML TextField author_affiliation;
	@FXML Label alabel;
	@FXML void submitAction(ActionEvent e) throws SQLException{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		ResultSet rs = stmt.executeQuery("SELECT count(*) from author where name = " + author_name.getText() + " and affiliation = " + author_affiliation.getText() + ";");
		if(rs.next()==false)
		{
			alabel.setText("");
			stmt.executeUpdate("INSERT INTO author (aid, name, affiliation) VALUES (" + IDGenerator.getAuthorCounter() + ",'" + author_name.getText() + "','" + author_affiliation.getText() + "');");
		}
		else
		{
			alabel.setText("Author with same affiliation already exists");
			alabel.setTextFill(Color.RED);
		}
		conn.close();
	}
}
