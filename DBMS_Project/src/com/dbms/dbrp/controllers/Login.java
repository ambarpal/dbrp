package com.dbms.dbrp.controllers;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.Main;
import com.dbms.dbrp.utilities.GlobalVariables;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Login {
	@FXML private Button submit_button, register_button;
	@FXML private Label login_prompt;
	@FXML private PasswordField password;
	@FXML private TextField username;
	static final String DB_URL = GlobalVariables.DB_URL;
	static final String USER = GlobalVariables.USER;
	static final String PASS = GlobalVariables.PASS;

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
		conn = DriverManager.getConnection(DB_URL, USER, PASS);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getCatalogs();
		flag = 0;
		count = 0;
		usr = username.getText();
		pwd = password.getText();
		md = MessageDigest.getInstance("SHA-256");
		md.update(pwd.getBytes());
		byteData = md.digest();
		sb = new StringBuffer();
		for(int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		}
		while(rs.next())
		{
			dbname = rs.getString(1).trim();
			if(dbname.equalsIgnoreCase("dbmsProject"))
				flag = 1;
		}
		return flag;
	}
	
	void openSearch() throws IOException
	{
		stage.close();
		TabPane a = (TabPane)FXMLLoader.load(Main.class.getResource("views/Search.fxml"));
		Scene s = new Scene(a);
		Stage app_stage = new Stage();
		app_stage.hide();
		app_stage.setScene(s);
		app_stage.show();
	}
	
	@FXML void login(ActionEvent e) throws SQLException, NoSuchAlgorithmException, IOException
	{
		initVariables();
		source = (Node)e.getSource();
		stage = (Stage)source.getScene().getWindow();
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
			if(count == 1)	//DB has user with this username
			{
				login_prompt.setText("Successfully logged in");
				login_prompt.setTextFill(Color.GREEN);
				openSearch();
			}
			else
			{
				login_prompt.setText("Incorrect username/password");
				login_prompt.setTextFill(Color.RED);
			}
		}
	}
	
	@FXML void register(ActionEvent e) throws SQLException, NoSuchAlgorithmException, IOException
	{
		initVariables();
		source = (Node)e.getSource();
		stage = (Stage)source.getScene().getWindow();
		if(flag == 0)
		{
			sql = "CREATE DATABASE dbmsProject;";
			stmt.executeUpdate(sql);
			sql = "USE dbmsProject;";
			stmt.executeQuery(sql);
			sql = "CREATE TABLE users "
			+"(username VARCHAR(400) PRIMARY KEY, "
//			+"isAdmin TINYINT, "
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
				openSearch();
			}
			else
			{
				login_prompt.setText("Choose a different username");
				login_prompt.setTextFill(Color.RED);
			}
		}
	}
}
