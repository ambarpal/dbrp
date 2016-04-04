/**
 * 
 */
package com.dbms.dbrp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author ambar14012, harshvardhan14043
 *
 */
public class SearchUploadController {
	@FXML private TextArea title_u;
	@FXML private TextArea authors_u;
	@FXML private TextArea abstract_u;
	@FXML private TextArea citations_u;
	@FXML void uploadPaper(ActionEvent e){
		FileChooser fc = new FileChooser();
		fc.setTitle("Open paper");
		fc.showOpenDialog(new Stage());
	}
}
