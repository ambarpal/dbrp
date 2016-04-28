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
import javafx.geometry.Pos;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class ConferenceController {
	@FXML TextField conf_name;
	@FXML DatePicker conf_date;
	@FXML Label clabel; 
	@FXML void submitAction(ActionEvent e) throws SQLException{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		ResultSet rs = stmt.executeQuery("SELECT count(*) as count from conference where name = '" + conf_name.getText() + "' and cdate = '" + conf_date.getValue().toString() + "';");
		if(rs.next())
		{
			if(rs.getInt("count") == 0)
			{
				stmt.executeUpdate("INSERT INTO conference (cid, name, cdate) VALUES (" + IDGenerator.getConferenceCounter() + ",'" + conf_name.getText() + "','" + conf_date.getValue().toString() + "');");
				clabel.setText("Conference successfully added");
				clabel.setAlignment(Pos.CENTER);
				clabel.setTextFill(Color.GREEN);
			}
			else
			{
				clabel.setText("Conference with same date already exists");
				clabel.setAlignment(Pos.CENTER);
				clabel.setTextFill(Color.RED);
			}
		}
		conn.close();
	}
}
