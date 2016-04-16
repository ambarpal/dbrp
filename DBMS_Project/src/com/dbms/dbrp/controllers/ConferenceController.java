package com.dbms.dbrp.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;
import com.dbms.dbrp.utilities.IDGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class ConferenceController {
	@FXML TextField conf_name;
	@FXML DatePicker conf_date;
	@FXML void submitAction(ActionEvent e) throws SQLException{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		stmt.executeUpdate("INSERT INTO conference (cid, name, date) VALUES (" + IDGenerator.getConferenceCounter() + ",'" + conf_name.getText() + "','" + conf_date.getValue().toString() + "');");
		conn.close();
	}
}
