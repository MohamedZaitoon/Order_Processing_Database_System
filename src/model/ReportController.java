package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.jdbc.CallableStatement;

public class ReportController {

	
	private Connection con;
	
	public static enum OperationType{TotalSales, TopFiveUser, TopTenBooks}
	
	public ReportController() {
		con = DatabaseConnection.getConnection();
	}
	
	
	public OperationResponse getDataOfOperation(OperationType opt) {
		OperationResponse or = new OperationResponse();
		try {
			String query = getQueryOfOperation(opt);
			CallableStatement stmt = (CallableStatement) con.prepareCall(query);
			ResultSet rs = stmt.executeQuery();
			or.setExecutedSuccessfuly(true);
			or.setResultSet(rs);
		}catch (SQLException e) {
			or.setExecutedSuccessfuly(false);
			or.setErrorMessage(e.getMessage());
			e.printStackTrace();
		}
		return or;
	}	
	
	private String getQueryOfOperation(OperationType opt) {
			
		switch(opt) {
		
			case TotalSales:
				return "CALL total_sales()";
			case TopFiveUser:
				return "CALL top_five()";
			case TopTenBooks:
				return "CALL top_10_books()";
			default :
				return "";
		}		
	}
	
	public boolean isConnected() {
		return con != null;
	}
	
	
}
