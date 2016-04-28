package com.dbms.dbrp.controllers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.dbms.dbrp.utilities.GlobalVariables;
import com.dbms.dbrp.utilities.IDGenerator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * @author ambar14012, harshvardhan14043
 */
public class UploadController {
	@FXML private TextArea title_u;
	@FXML private TextArea authors_u;
	@FXML private TextArea citations_u;
	@FXML private TextArea conference_u;
	@FXML private Label uploadLabel;
	@FXML private Button submit,uploadPaper;
	
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
    Boolean isUploaded;
    
    public UploadController() throws SQLException{
    	initVariables();
	}
	int initVariables() throws SQLException
	{
		conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		stmt = conn.createStatement();
		rs = conn.getMetaData().getCatalogs();
		while(rs.next())
		{
			dbname = rs.getString(1).trim();
			if(dbname.equalsIgnoreCase("dbmsProject"))
				flag = 1;
		}
		isUploaded = false;
		return flag;
	}
	
	void fixCitations() throws SQLException{
		// Fix all citations
		rs = stmt.executeQuery("SELECT distinct p1_pid from isCitationOf;");
		ArrayList<String> p1 = new ArrayList<>();
		while(rs.next())
			p1.add(rs.getString(1));
		int count=0;
		for(int i=0;i<p1.size();i++)
		{
			rs = stmt.executeQuery("SELECT count(*) as count from isCitationOf where p1_pid = " + p1.get(i) + ";");
			if(rs.next())
				count = rs.getInt("count");
			stmt.executeUpdate("UPDATE papers set citationCount = " + count + " where pid = " + p1.get(i) + ";");
		}
	}
//	int keyWordExists(String keyword){
//		// return kid if keyWord exists in the Database else -1
//		
//		return -1;
//	}
	int conferenceExists(String conference, String date) throws SQLException{
		// return cid id conference exists in the Database else -1
		rs = stmt.executeQuery("select cid from conference where name = " + conference + " and cdate = " + date + ";");
		if(rs.next())
			return rs.getInt("cid");
		return -1;
	}
	int paperExists(String conference) throws SQLException
	{
		// return cid if paper exists, else -1
		rs = stmt.executeQuery("select cid from conference where name = " + conference + ";");
		if(rs.next())
			return rs.getInt("cid");
		return -1;
	}
	@FXML void uploadPaper(ActionEvent e) throws IOException, SQLException
	{
		System.out.println(conn.toString());
		// Begin To Delete
    	title_u.setText("ASDJFHDSAHfkahsdjfhsdakjhfjasdkjfhakdjsgfkdakjs");
    	authors_u.setText("1,2");
    	citations_u.setText("3,4,5");
    	conference_u.setText("10");
    	// End To Delete

		FileChooser fc = new FileChooser();
		fc.setTitle("Open paper");
		BufferedReader b = new BufferedReader(new FileReader("data/counter"));
		String temp = b.readLine().toString();
		int count = Integer.parseInt(temp);
		b.close();
		File file = fc.showOpenDialog(null), cp = new File("data/" + count++ + ".pdf");
		if (file != null) {
			if (file.toString().endsWith("pdf")) {
				Files.copy(file.toPath(), cp.toPath());
				isUploaded = true;
				System.out.println(isUploaded);
				uploadLabel.setText("PDF uploaded");
				uploadLabel.setTextFill(Color.GREEN);
			}
			else
			{
				uploadLabel.setText("Please choose a PDF file");
				uploadLabel.setTextFill(Color.RED);
			}
		}
		System.out.println(isUploaded);
		BufferedWriter br = new BufferedWriter(new FileWriter("data/counter"));
		br.write(count+"");
		br.close();
	}
	
	@FXML void submit(ActionEvent e) throws SQLException{
		System.out.println(conn.toString());
		System.out.println(isUploaded);
		String title = title_u.getText();
		String[] authorIds = authors_u.getText().split(",");
		String[] citationIds = citations_u.getText().split(",");
		String conference = conference_u.getText();
		Boolean flag2 = (title.length() == 0 || authorIds.length == 0 || conference.length() == 0 ||  citationIds.length == 0);
		stmt.execute("USE dbmsProject");
		if(flag2)
		{
			uploadLabel.setText("Enter all fields before submitting");
			uploadLabel.setTextFill(Color.RED);
		}
		else if (!isUploaded){
			uploadLabel.setText("You need to submit a pdf");
			uploadLabel.setTextFill(Color.RED);	
		}
		else
		{
			uploadLabel.setText("");
			
			// Insert Paper into Paper Table
			int paperId = IDGenerator.getPaperCounter();
			sql = "INSERT into papers values( " + paperId + " , '" + title + "' , " + 0 + " );";
//			System.out.println(sql);
			
			fixCitations();
//			System.out.println(conn.toString());
//			System.out.println(stmt.toString());
			stmt.executeUpdate(sql);
			
			// Insert keywords into table
//			for (String keyWord : abstractText.split(" ")){
//				int id = keyWordExists(keyWord);
//				if (id == -1){
//					sql = "INSERT into keyword values( " + IDGenerator.getKeywordCounter()
//						  + "," + keyWord + ");";
//					stmt.executeUpdate(sql);
//				}
//			}
			
			// Update isAuthorOf
			for (String s : authorIds) stmt.executeUpdate("INSERT into isAuthorOf values( " + Integer.parseInt(s) + " , " + paperId + ");");
			
			// Update isCitationOf
			for (String s: citationIds) stmt.executeUpdate("INSERT into isCitationOf values( " + Integer.parseInt(s) + " , " + paperId + ");");
			
			// Update isPublishedIn
			stmt.executeUpdate("INSERT into isPublishedIn values( " + paperId + " , " + Integer.parseInt(conference) + ");");
			
			// Update isKeywordIn
//			for (String keyWord : abstractText.split(" ")){
//				int id = keyWordExists(keyWord);
//				stmt.executeUpdate("INSERT into isKeywordIn values( " + id + " , " + paperId + ");");
//			}
		}
	}
}
