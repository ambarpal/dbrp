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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
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
	@FXML Button as_search;
	@FXML Button raw_search;
	@FXML Button search;
	@FXML Label as_label;
	@FXML Label alabel;
	@FXML Label plabel;
	@FXML TextArea res;
	@FXML TextArea cq_res;
	@FXML Button complex_q1;
	@FXML TextField confQ;
	@FXML TextField confQ2;
	
    static final ObservableList<Author> data = FXCollections.observableArrayList();
    static final ObservableList<Paper> paper_data = FXCollections.observableArrayList();
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
			as_label.setAlignment(Pos.CENTER);
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
		res.setVisible(true);
		res.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		        res.setScrollTop(Double.MAX_VALUE);
		    }
		});
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		String rs_query_str = rs_query.getText();
		ResultSet rs = stmt.executeQuery(rs_query_str);
		String resString = "";
		resString += "\t\t\t";
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
			resString += rs.getMetaData().getColumnName(i).toString() + "\t\t\t";
		resString += "\n";
		Integer lineNo = 1;
		while(rs.next())
		{
			int cols = rs.getMetaData().getColumnCount();
			resString += lineNo.toString() + "\t\t\t";
			lineNo += 1;
			for(int i = 0;i < cols; i++)
				resString += rs.getString(i + 1) + "\t\t\t";
			resString += "\n";
		}
		res.setText(resString);
		res.appendText("");
		conn.close();
	}
	
	@FXML void ps_search_action(ActionEvent e) throws SQLException, IOException
	{
		String paperid = ps_author.getText();
		String title = ps_title.getText();
		if(paperid.length() == 0 && title.length() == 0)
		{
			plabel.setText("Enter at least one field");
			plabel.setAlignment(Pos.CENTER);
			plabel.setTextFill(Color.RED);
		}
		else
		{
			Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
			Statement stmt = conn.createStatement();
			ResultSet rs = null;
			stmt.executeQuery("USE dbmsProject;");
			Boolean flag=false;
			for(int i=0;i<paperid.length();i++)
				if(paperid.charAt(i)<'0' || paperid.charAt(i)>'9')
					flag=true;
			if(flag)
			{
				plabel.setText("Author ID should be numeric");
				plabel.setAlignment(Pos.CENTER);
				plabel.setTextFill(Color.RED);
			}
			else
			{
				plabel.setText("");
				if(paperid.length() != 0 && title.length() != 0)
					rs = stmt.executeQuery("SELECT * from papers where pid = " + paperid + " and title like '%" + title +"%';");
				else if(paperid.length() != 0)
					rs = stmt.executeQuery("SELECT * from papers where pid = " + paperid + ";");
				else
					rs = stmt.executeQuery("SELECT * from papers where title like '%" + paperid + "%';");
				paper_data.clear();
				while(rs.next())
					paper_data.add(new Paper(Integer.parseInt(rs.getString("pid")), rs.getString("title")));
				displayPapers();
			}
			conn.close();
		}
	}
	
	void executeComplexQuery() throws SQLException{
		cq_res.setVisible(true);
		cq_res.textProperty().addListener(new ChangeListener<Object>() {
		    @Override
		    public void changed(ObservableValue<?> observable, Object oldValue,
		            Object newValue) {
		        cq_res.setScrollTop(Double.MAX_VALUE);
		    }
		});
		Connection conn = DriverManager.getConnection(GlobalVariables.DB_URL, GlobalVariables.USER, GlobalVariables.PASS);
		Statement stmt = conn.createStatement();
		stmt.executeQuery("USE dbmsProject;");
		String rs_query_str = rs_query.getText();
		ResultSet rs = stmt.executeQuery(rs_query_str);
		String resString = "";
		resString += "\t\t\t";
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++)
			resString += rs.getMetaData().getColumnName(i).toString() + "\t\t\t";
		resString += "\n";
		Integer lineNo = 1;
		while(rs.next())
		{
			int cols = rs.getMetaData().getColumnCount();
			resString += lineNo.toString() + "\t\t\t";
			lineNo += 1;
			for(int i = 0;i < cols; i++)
				resString += rs.getString(i + 1) + "\t\t\t";
			resString += "\n";
		}
		cq_res.setText(resString);
		cq_res.appendText("");
		conn.close();
	}
	@FXML void complex_action1(ActionEvent e) throws SQLException{
		rs_query.setText("select ath.name from author as ath where (select count(*) as c from author natural join isAuthorOf where author.aid = ath.aid) >= 2;");
		executeComplexQuery();
	}
	@FXML void complex_action2(ActionEvent e) throws SQLException{
		rs_query.setText("select name, title from author natural join isAuthorOf natural join papers where citationcount >= 10;");
		executeComplexQuery();
	}
	@FXML void complex_action3(ActionEvent e) throws SQLException{
		rs_query.setText("select distinct p.title from papers as p where (select count(*) from author natural join isAuthorOf natural join papers where papers.pid = p.pid) >= 2;");
		executeComplexQuery();
	}
	@FXML void complex_action4(ActionEvent e) throws SQLException{
		System.out.println(confQ.getText());
		rs_query.setText("select ath.name from author as ath where ath.aid in (select aid from papers natural join isAuthorOf natural join conference authors natural join isPublishedIn where cid = "+ confQ.getText() + ");");
		executeComplexQuery();
	}
	@FXML void complex_action5(ActionEvent e) throws SQLException{
		rs_query.setText("select title from papers where pid in (select p1_pid from isCitationOf where p2_pid in (select distinct pid from papers natural join isAuthorOf natural join conference authors natural join isPublishedIn where cid = " + confQ2.getText() + "));");
		executeComplexQuery();
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
	
	void displayPapers() throws IOException {
		AnchorPane a = (AnchorPane)FXMLLoader.load(Main.class.getResource("views/PaperResults.fxml"));
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
    
    public static class Paper
    {
    	private SimpleIntegerProperty pid;
		private final SimpleStringProperty title;
        private Paper(int pid, String title) {
        	this.pid = new SimpleIntegerProperty(pid);
            this.title = new SimpleStringProperty(title);
        }
        public Integer getPid() { return pid.get();}
		public void setPid(int pid_) { pid.set(pid_); }
        public String getTitle() {return title.get();}
        public void setTitle(String title_) { title.set(title_);}
    }
}
