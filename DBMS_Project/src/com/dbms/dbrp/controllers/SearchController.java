package com.dbms.dbrp.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.Main;
import com.dbms.dbrp.utilities.GlobalVariables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SearchController {
	@FXML TextArea rs_query;
	@FXML TextField as_name;
	@FXML TextField as_affiliation;
	@FXML TextField ps_author;
	@FXML TextField ps_title;
	@FXML TextField ps_keywords;
	@FXML Button as_search;
	@FXML Button raw_search;
	@FXML Button search;
	@FXML Label as_label;
	@FXML Label alabel;
	@FXML Label plabel;

    static final ObservableList<Author> data = FXCollections.observableArrayList();
    static final ObservableList<Author> raw_data = FXCollections.observableArrayList();
	@FXML void as_search_action(ActionEvent e) throws SQLException, IOException{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		ResultSet rs = null;
		String authorname = as_name.getText();
		String affiliationname = as_affiliation.getText();
		if(authorname.length() == 0 && affiliationname.length() == 0)
		{
			as_label.setText("Enter at least one search field");
			as_label.setTextFill(Color.RED);
		}
		else
		{
			as_label.setText("");
			if(authorname.length() != 0 && affiliationname.length() != 0)
				rs = stmt.executeQuery("select * from author where name like '%" + as_name.getText() + "%' and affiliation like '%" + as_affiliation.getText() + "%';");
			else if(authorname.length() != 0)
				rs = stmt.executeQuery("select * from author where name like '%" + as_name.getText() + "%';");
			else
				rs = stmt.executeQuery("select * from author where affiliation like '%" + as_affiliation.getText() + "%';");
			data.clear();
			while (rs.next())
				data.add(new Author(Integer.parseInt(rs.getString("aid")), rs.getString("name"), rs.getString("affiliation")));
			displayAuthors();
		}
		conn.close();
	}
	
	@FXML void rs_search_action(ActionEvent e) throws SQLException, IOException
	{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		String rs_query_str = rs_query.getText();
		ResultSet rs = stmt.executeQuery(rs_query_str);
		while(rs.next())
		{
			int cols = rs.getMetaData().getColumnCount();
			for(int i = 0;i < cols;i++)
				System.out.print(rs.getString(i + 1) + " ");
			System.out.print("\n");
		}
		conn.close();
	}
	
	@FXML void ps_search_action(ActionEvent e) throws SQLException
	{
		String author = ps_author.getText();
		String title = ps_title.getText();
		String[] keywords = ps_keywords.getText().split(" ");
		if(author.length() == 0 && title.length() == 0 && keywords.length == 0)
		{
			plabel.setText("Enter at least one field");
			plabel.setTextFill(Color.RED);
		}
		else
		{
			Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
			Statement stmt = conn.createStatement();
			stmt.executeQuery("USE dbmsProject;");

			conn.close();
		}
	}
	
	void displayAuthors() throws IOException {
		
		AnchorPane a = (AnchorPane)FXMLLoader.load(Main.class.getResource("views/SearchResults.fxml"));
		Scene s = new Scene(a);
		Stage app_stage = new Stage();
		app_stage.hide();
		app_stage.setTitle("Query Results");
		app_stage.setScene(s);
		app_stage.show();
	}
 
    public static class Author {
    	private SimpleIntegerProperty aid;
		private final SimpleStringProperty name;
        private final SimpleStringProperty affiliation;
 
        private Author(int aid, String name, String affiliation) {
        	this.aid = new SimpleIntegerProperty(aid);
            this.name = new SimpleStringProperty(name);
            this.affiliation = new SimpleStringProperty(affiliation);
        }
        
        public Integer getAid() { return aid.get();}
		public void setAid(int aid_) { aid.set(aid_); }
        public String getName() {return name.get();}
        public void setName(String name_) { name.set(name_);}
        public String getAffiliation(){ return affiliation.get();}
        public void setAffiliation(String affiliation_) {affiliation.set(affiliation_);}
    }
}
