package com.dbms.dbrp.controllers;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class Login {
	@FXML private Button submit_button, register_button;
	@FXML private Label login_prompt;
	@FXML private PasswordField password;
	@FXML private TextField username;
	static final String DB_URL = GlobalVariables.DB_URL;
	static final String USER = GlobalVariables.USER;
	static final String PASS = GlobalVariables.PASS;
	@FXML void login(ActionEvent e) throws SQLException, NoSuchAlgorithmException
	{
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		ResultSet rs = conn.getMetaData().getCatalogs();
		int flag = 0, count = 0;
		String sql, dbname, usr = username.getText(), pwd = password.getText();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(pwd.getBytes());
		byte byteData[]=md.digest();
		StringBuffer sb = new StringBuffer();
        for(int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        while(rs.next())
		{
			dbname = rs.getString(1).trim();
			if(dbname.equalsIgnoreCase("dbmsProject"))
				flag = 1;
		}
		if(flag == 0)
		{
			login_prompt.setText("Register yourself first");
			login_prompt.setTextFill(Color.RED);
		}
		else
		{
			sql = "USE dbmsProject;";
			stmt.executeQuery(sql);
		}
		if(usr.length() == 0 || pwd.length() == 0)
		{
			login_prompt.setText("Please enter a username/password");
			login_prompt.setTextFill(Color.RED);
		}
		else if(flag != 0)
		{
			pwd = sb.toString();
			sql = "SELECT count(*) FROM users WHERE username = '" + usr + "' and password = '" + pwd + "';";
			rs = stmt.executeQuery(sql);
			if(rs.next())
				count = rs.getInt(1);
			if(count == 1)	//No user with this username or password
			{
				login_prompt.setText("Successfully logged in");
				login_prompt.setTextFill(Color.GREEN);
			}
			else
			{
				login_prompt.setText("Incorrect username/password");
				login_prompt.setTextFill(Color.RED);
			}
		}
	}
	@FXML void register(ActionEvent e) throws SQLException, NoSuchAlgorithmException
	{
		Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
		Statement stmt = conn.createStatement();
		ResultSet rs = conn.getMetaData().getCatalogs();
		int flag = 0, count = 0;
		String sql, dbname, usr = username.getText(), pwd = password.getText();
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(pwd.getBytes());
		byte byteData[]=md.digest();
		StringBuffer sb = new StringBuffer();
        for(int i = 0; i < byteData.length; i++) {
        	sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
		while(rs.next())
		{
			dbname = rs.getString(1).trim();
			if(dbname.equalsIgnoreCase("dbmsProject"))
				flag = 1;
		}
		if(flag == 0)
		{
			sql = "CREATE DATABASE dbmsProject;";
			stmt.executeUpdate(sql);
			sql = "USE dbmsProject;";
			stmt.executeQuery(sql);
			sql = "CREATE TABLE users "
			+"(username VARCHAR(400) PRIMARY KEY, "
			+"password VARCHAR(400));"; 
			stmt.executeUpdate(sql);
		}
		else
		{
			sql = "USE dbmsProject;";
			stmt.executeQuery(sql);
		}
		if(usr.length() == 0 || pwd.length() == 0)
		{
			login_prompt.setText("Please enter a username/password");
			login_prompt.setTextFill(Color.RED);
		}
		else if(pwd.length()<6)
		{
			login_prompt.setText("Password should be at least 6 characters");
			login_prompt.setTextFill(Color.RED);
		}
		else
		{
			pwd = sb.toString();
			sql = "SELECT count(*) FROM users WHERE username = '" + usr + "';";
			rs = stmt.executeQuery(sql);
			if(rs.next())
				count = rs.getInt(1);
			if(count == 0)	//No user with this username
			{
				sql = "INSERT INTO users"
				+ "(username, password) VALUES"
				+ "(?, ?);";
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, usr);
				ps.setString(2, pwd);
				ps.executeUpdate();
				login_prompt.setText("Successfully registered");
				login_prompt.setTextFill(Color.GREEN);
			}
			else
			{
				login_prompt.setText("Choose a different username");
				login_prompt.setTextFill(Color.RED);
			}
		}
	}
}
