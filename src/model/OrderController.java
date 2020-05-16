package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.mysql.cj.jdbc.CallableStatement;

public class OrderController {

		private Connection con;
		
		public OrderController() {
			con = DatabaseConnection.getConnection();
		}
		
		
		public OperationResponse getOrders() {
			OperationResponse or = new OperationResponse();
			try {
				String query = "SELECT * FROM orders";
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				or.setExecutedSuccessfuly(true);
				or.setResultSet(rs);
			}catch (SQLException e) {
				or.setExecutedSuccessfuly(false);
				or.setErrorMessage(e.getMessage());
				e.printStackTrace();
			}
			
			return or;
		}
		
		
		public OperationResponse confirmOrder(Integer idOrder) {
			OperationResponse or = new OperationResponse();
			try {
				String query = "CALL confirm_order(?)";
				CallableStatement stmt = (CallableStatement) con.prepareCall(query);
				stmt.setInt(1, idOrder);
				stmt.execute();
				or.setExecutedSuccessfuly(true);
			}catch (SQLException e) {
				or.setExecutedSuccessfuly(false);
				or.setErrorMessage(e.getMessage());
				e.printStackTrace();
			}
			
			return or;
		}

		public boolean isConnected() {
			return con != null;
		}
		
		
		
}
