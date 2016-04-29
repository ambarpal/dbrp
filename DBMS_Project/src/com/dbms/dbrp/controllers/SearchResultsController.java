package com.dbms.dbrp.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.dbms.dbrp.controllers.SearchController.Author;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchResultsController implements Initializable{
	@FXML TableColumn<TableView<Author>, String> aidColumn;
	@FXML TableColumn<TableView<Author>, String> nameColumn;
	@FXML TableColumn<TableView<Author>, String> affiliationColumn;
	@FXML TableView<Author> table;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		aidColumn.setCellValueFactory(new PropertyValueFactory<TableView<Author>,String>("aid"));
		nameColumn.setCellValueFactory(new PropertyValueFactory<TableView<Author>,String>("name"));
		affiliationColumn.setCellValueFactory(new PropertyValueFactory<TableView<Author>,String>("affiliation"));
		table.setItems(SearchController.data);
	}
}
