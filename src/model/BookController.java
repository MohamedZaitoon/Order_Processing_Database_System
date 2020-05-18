package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
			
			CallableStatement stmt = con.prepareCall("{CALL add_book(?,?,?,?,?,?,?,?)}");
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
			String query = "INSERT INTO authors VALUES ";
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
	
	public OperationResponse deleteAuthors(String ISBN, ArrayList<String> authors) {
		OperationResponse or = new OperationResponse();
		if(ISBN == null||ISBN.isEmpty() || authors == null ||authors.isEmpty()) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage("Error: empty or null parameters");
			return or;
		}
		try { 
			String query = "DELETE FROM authors WHERE ISBN = ? AND author_name = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			for(String name : authors) {
				stmt.setString(1, ISBN);
				stmt.setString(1, name);
				stmt.executeUpdate();
			}
			or.setExecutedSuccessfuly(true);
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
			ResultSet q = stmt.executeQuery("DESC book category");
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
	
	public OperationResponse searchForBook(String isbn) {
		OperationResponse or = new OperationResponse();
		try {
			String query = "{CALL search_by_isbn(?)}";
			CallableStatement stmt = (CallableStatement) con.prepareCall(query,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY );
			
			stmt.setString(1, isbn);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()) {
				or.setExecutedSuccessfuly(false);
				or.setErrorMessage("Book with this ISBN does not exist.");
			}else {
				or.setExecutedSuccessfuly(true);
				rs.beforeFirst();
				or.setResultSet(rs);				
			}
		}catch (SQLException e) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return or;
	}
	
	
	public Book getBookInstance(ResultSet rs) {
		Book book;
		try {
			rs.next();
			String ISBN =rs.getString("ISBN");
			String title = rs.getString("title");
			String publisher = rs.getString("publisher");
			String category = rs.getString("category");
			String year = rs.getString("pyear").substring(0, 4);
			Integer noCopies =  rs.getInt("no_copies");
			Integer threshold = rs.getInt("threshold");
			Integer sellingPrice= rs.getInt("selling_price");
			book = new Book(ISBN,title, category, sellingPrice, noCopies,threshold,publisher, year);
			return book;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;	
	}
	
	
	public OperationResponse updateBook(Book book) {
		OperationResponse or = new OperationResponse();
		if(book == null) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage("Error: null parameter");
			return or;
		}
		try { 
			
			//`update_book`(_old_ISBN,bISBN ,bTitle ,bCategory , bSelling_price ,
			//bno_copies ,bthreshold , bpublisher,bpyear) 
			CallableStatement stmt = con.prepareCall("{CALL update_book(?,?,?,?,?,?,?,?,?)}");
			stmt.setString(1, book.getISBN());
			stmt.setString(2, book.getISBN());
			stmt.setString(3,  book.getTitle());
			stmt.setString(4, book.getCategory());
			stmt.setInt(5, book.getSellingPrice());
			stmt.setInt(6, book.getNoCopies());
			stmt.setInt(7, book.getThreshold());
			stmt.setString(8, book.getPublisher());
			stmt.setString(9, book.getPublicationYear());
			int n = stmt.executeUpdate();
			or.setExecutedSuccessfuly(n == 1);
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
	
	public ArrayList<String> getAuthorOfBook(String ISBN){
		ArrayList<String> authors = new ArrayList<>();
		try {
			String query = "SELECT author_name FROM authors WHERE ISBN = ?";
			PreparedStatement stmt = con.prepareStatement(query);
			stmt.setString(1, ISBN);
			ResultSet rs = stmt.executeQuery();
			while(rs.next()) {
				authors.add(rs.getString(1));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return authors;
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
