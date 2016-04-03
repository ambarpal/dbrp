package com.dbms.dbrp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application{
	@Override
	public void start(Stage primaryStage) {
		try
		{
//			AnchorPane a=(AnchorPane)FXMLLoader.load(Main.class.getResource("views/Login.fxml"));
			TabPane a=(TabPane)FXMLLoader.load(Main.class.getResource("views/Search.fxml"));
			Scene s=new Scene(a);
			primaryStage.setScene(s);
			primaryStage.show();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
