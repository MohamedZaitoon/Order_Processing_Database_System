package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;

public class BookController {
	
	private Connection con;
	
	public BookController(){
		con = DatabaseConnection.getConnection();
	}
	
	public OperationResponse addBook(Book book) {
		OperationResponse or = new OperationResponse();
		if(book == null) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage("Error: null parameter");
			return or;
		}
		try { 
			//`add_book`(bISBN ,bTitle ,bCategory , bSelling_price ,
			//bno_copies ,bthreshold , bpublisher,bpyear) 
			
			CallableStatement stmt = con.prepareCall("{call add_book(?,?,?,?,?,?,?,?)}");
			stmt.setString(1, book.getISBN());
			stmt.setString(2,  book.getTitle());
			stmt.setString(3, book.getCategory());
			stmt.setInt(4, book.getSellingPrice());
			stmt.setInt(5, book.getNoCopies());
			stmt.setInt(6, book.getThreshold());
			stmt.setString(7, book.getPublisher());
			stmt.setString(8, book.getPublicationYear());
			stmt.execute();
			or.setExecutedSuccessfuly(true);
		}catch(SQLIntegrityConstraintViolationException e) {
			or.setExecutedSuccessfuly(false);
			String msg = e.getMessage();
			if(msg.contains("publisher")) {
				or.setErrorMessage("Publisher does not exist");				
			}else if(msg.contains("Duplicate entry")) {
				or.setErrorMessage("Book already exist with the same ISBN");	
			}
//			System.out.println(e.getMessage());
		} catch (SQLException e) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage(e.getMessage());
//			e.printStackTrace();
		}
		
		return or;
	}
	
	public OperationResponse addAuthors(String ISBN, ArrayList<String> authors) {
		OperationResponse or = new OperationResponse();
		if(ISBN == null||ISBN.isEmpty() || authors == null ||authors.isEmpty()) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage("Error: empty or null parameters");
			return or;
		}
		try { 
			String query = "insert into authors values ";
			for(String author: authors) 
				query+= "('" +author+"','"+ISBN+"')";
			query = query.replaceAll("\\)\\(", "),(");
			Statement stmt = con.createStatement();
			stmt.execute(query);
			or.setExecutedSuccessfuly(true);
			System.out.println(query);
		}catch (SQLException e) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return or;
	}
	
	public String[] getCategories() {
		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet q = stmt.executeQuery("desc book category");
			q.next();
			String	cats =  q.getString(2).replaceAll("\\)", "").replaceAll("\\(", "").replace("enum","");
			String[] arr = cats.split(",");
			for(int i = 0; i < arr.length ; i++) {
				arr[i] = arr[i].replaceAll("'", "");
			}
			return arr;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	/*
	 * Checking if the database is connected.
	 */
	public boolean isConnected() {
		return con != null;
	}
	
	public int getTitleMaxLenth() {
		// TODO: get length of title from database and return it
		return 80;
	}
	
	public int getISBNMaxLength() {
		// TODO: get length of ISBN from database and return it
		return 13;
	}
	
	public int getPublisherMaxLength() {
		// TODO: get length of publisher from database and return it
		return 60;
	}



	public int getAuthorMaxLength() {
		// TODO: get length of author from database and return it
		return 60;
	}
}
