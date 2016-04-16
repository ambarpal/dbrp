package com.dbms.dbrp.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;
import com.dbms.dbrp.utilities.IDGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AuthorController {
	@FXML TextField author_name;
	@FXML TextField author_affiliation;
	@FXML void submitAction(ActionEvent e) throws SQLException{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		stmt.executeUpdate("INSERT INTO author (aid, name, affiliation) VALUES (" + IDGenerator.getAuthorCounter() + ",'" + author_name.getText() + "','" + author_affiliation.getText() + "');");
		conn.close();
	}
}
