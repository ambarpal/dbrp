/**
 * 
 */
package com.dbms.dbrp.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

/**
 * @author ambar14012, harshvardhan14043
 *
 */
public class SearchUploadController {
	@FXML private TextArea title_u;
	@FXML private TextArea authors_u;
	@FXML private TextArea abstract_u;
	@FXML private TextArea citations_u;
	@FXML void uploadPaper(ActionEvent e) throws IOException{
		FileChooser fc = new FileChooser();
		fc.setTitle("Open paper");
		int count = 1;
		File file = fc.showOpenDialog(null), cp = new File("data/" + count++ + ".pdf");
		if (file != null) {
			if (file.toString().endsWith("pdf")) {
				Files.copy(file.toPath(), cp.toPath());
			}
		}
	}
}
