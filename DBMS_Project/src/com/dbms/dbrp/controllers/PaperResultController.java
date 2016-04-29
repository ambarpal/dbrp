package com.dbms.dbrp.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import com.dbms.dbrp.controllers.SearchController.Paper;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PaperResultController implements Initializable{
	@FXML TableColumn<TableView<Paper>, String> pidColumn;
	@FXML TableColumn<TableView<Paper>, String> titleColumn;
	@FXML TableView<Paper> ptable;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		pidColumn.setCellValueFactory(new PropertyValueFactory<TableView<Paper>, String>("pid"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<TableView<Paper>, String>("title"));
		ptable.setItems(SearchController.paper_data);	 
	}
}
