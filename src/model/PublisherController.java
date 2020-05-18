package model;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class PublisherController {

		private Connection con;
		
		public PublisherController() {
			con = DatabaseConnection.getConnection();
		}
		
		public OperationResponse addPublisher(Publisher publisher) {
			OperationResponse or = new OperationResponse();
			if(publisher == null) {
				or.setExecutedSuccessfuly(false);
				or.setErrorMessage("Error: null parameter");
				return or;
			}
			try { 
				CallableStatement stmt = con.prepareCall("{CALL add_publisher(?,?,?)}");
				stmt.setString(1,publisher.getName());
				stmt.setString(2,publisher.getAddress());
				stmt.setString(3,publisher.getPhone());
				stmt.execute();
				or.setExecutedSuccessfuly(true);
			}catch(SQLIntegrityConstraintViolationException e) {
				or.setExecutedSuccessfuly(false);
				String msg = e.getMessage();
				if(msg.contains("Duplicate entry")) {
					or.setErrorMessage("Publisher already exists");	
				}
//				System.out.println(e.getMessage());
			} catch (SQLException e) {
				or.setExecutedSuccessfuly(false);
				or.setErrorMessage(e.getMessage());
//				e.printStackTrace();
			}
			
			return or;
		}
		
		
		public int getNameMaxLenth() {
			// TODO: get length of publisher name from database and return it
			return 60;
		}
		
		public int getAddressMaxLenth() {
			// TODO: get length of publisher name from database and return it
			return 90;
		}
		
		public int getPhoneMaxLenth() {
			// TODO: get length of publisher name from database and return it
			return 30;
		}
		
		
		
		
		
		
		public boolean isConnected() {
			return con != null;
		}
		
}
