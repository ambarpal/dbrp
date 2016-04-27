package com.dbms.dbrp.controllers;

import java.io.IOException;

import com.dbms.dbrp.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ActionController {
	
	@FXML void searchAction(ActionEvent e) throws IOException{
		TabPane a = (TabPane)FXMLLoader.load(Main.class.getResource("views/Search.fxml"));
		Scene s = new Scene(a);
		Stage app_stage = new Stage();
		app_stage.hide();
		app_stage.setScene(s);
		app_stage.setTitle("Search");
		app_stage.show();
	}

	@FXML void uploadAction(ActionEvent e) throws IOException{
		AnchorPane a = (AnchorPane)FXMLLoader.load(Main.class.getResource("views/Upload.fxml"));
		Scene s = new Scene(a);
		Stage app_stage = new Stage();
		app_stage.hide();
		app_stage.setScene(s);
		app_stage.setTitle("Upload");
		app_stage.show();
	}
	@FXML void newAuthorAction(ActionEvent e) throws IOException{
		AnchorPane a = (AnchorPane)FXMLLoader.load(Main.class.getResource("views/newAuthor.fxml"));
		Scene s = new Scene(a);
		Stage app_stage = new Stage();
		app_stage.hide();
		app_stage.setScene(s);
		app_stage.setTitle("New Author");
		app_stage.show();
	}
	@FXML void newConferenceAction(ActionEvent e) throws IOException{
		AnchorPane a = (AnchorPane)FXMLLoader.load(Main.class.getResource("views/newConference.fxml"));
		Scene s = new Scene(a);
		Stage app_stage = new Stage();
		app_stage.hide();
		app_stage.setScene(s);
		app_stage.setTitle("New Conference");
		app_stage.show();
	}
}
