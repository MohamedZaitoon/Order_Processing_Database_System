package Model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class BookController {
	
	private Connection con;
	
	public BookController(){
		con = DatabaseConnection.getConnection();
	}
	
	OperationResponse addBook(Book book) {
		OperationResponse or = new OperationResponse();
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
		}catch (SQLException e) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage(e.toString());
		}
		
		return or;
	}
	
}
