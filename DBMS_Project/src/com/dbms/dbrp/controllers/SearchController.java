package com.dbms.dbrp.controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.dbms.dbrp.utilities.GlobalVariables;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SearchController {
	@FXML TextField as_name;
	@FXML TextField as_affiliation;
	@FXML Button as_search;
	@FXML Label as_label;

    private TableView<Author> table;
    private final ObservableList<Author> data = FXCollections.observableArrayList();
	@FXML void as_search_action(ActionEvent e) throws SQLException{
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		
		// TODO: Improve the query to handle empty name field or empty affiliation field
		ResultSet rs = null;
		String authorname = as_name.getText();
		String affiliationname = as_affiliation.getText();
		if(authorname.length() == 0 || affiliationname.length() == 0)
		{
			as_label.setText("Enter at least one search field");
			as_label.setTextFill(Color.RED);
		}
		else
		{
			if(authorname.length() != 0 && affiliationname.length() != 0)
				rs = stmt.executeQuery("select * from author where name like '" + as_name.getText() + "%' and affiliation like '" + as_affiliation.getText() + "%';");
			else if(authorname.length() != 0)
				rs = stmt.executeQuery("select * from author where name like '" + as_name.getText() + "%';");
			else
				rs = stmt.executeQuery("select * from author where affiliation like '" + as_affiliation.getText() + "%';");
		}
		
		data.clear();
		while (rs.next()){
			// System.out.println(rs.getString("aid") + rs.getString("name") + rs.getString("affiliation"));
			data.add(new Author(Integer.parseInt(rs.getString("aid")), rs.getString("name"), rs.getString("affiliation")));
		}
		displayAuthors();
		conn.close();
	}
	
	void displayAuthors() {
		// TODO: Instead of creating this by code, make a new fxml 
		// containing the empty table and load it here
        Scene scene = new Scene(new Group());
        Stage stage = new Stage();
        stage.setTitle("Query Results");
        stage.setWidth(600);
        stage.setHeight(800);
 
        final Label label = new Label("Author List");
 
        table = new TableView<Author>();
        table.setEditable(false);
 
        TableColumn aidColumn = new TableColumn("Author ID");
        aidColumn.setMinWidth(100);
        aidColumn.setCellValueFactory(
                new PropertyValueFactory<Author, Integer>("aid"));
 
        TableColumn nameColumn = new TableColumn("Author Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<Author, String>("name"));
 
        TableColumn affiliationColumn = new TableColumn("Affiliation");
        affiliationColumn.setMinWidth(200);
        affiliationColumn.setCellValueFactory(
                new PropertyValueFactory<Author, String>("affiliation"));
 
        table.setItems(data);
        table.getColumns().addAll(aidColumn, nameColumn, affiliationColumn);
 
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);
 
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
 
        stage.setScene(scene);
        stage.show();
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
