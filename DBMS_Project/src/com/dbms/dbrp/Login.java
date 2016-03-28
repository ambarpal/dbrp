package com.dbms.dbrp;

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
	@FXML void login(ActionEvent e)
	{
		if(username.getText().length()==0 || password.getText().length()==0)
		{
			login_prompt.setText("Please enter a username/password");
			login_prompt.setTextFill(Color.RED);
		}
	}
	@FXML void register(ActionEvent e)
	{
		if(username.getText().length()==0 || password.getText().length()==0)
		{
			login_prompt.setText("Please enter a username/password");
			login_prompt.setTextFill(Color.BROWN);
		}
	}
}
