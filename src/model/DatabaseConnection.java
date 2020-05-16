package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
	/*
	 * Connection to the database 
	 */
	private static Connection connection;
	/*
	 * Url to the databse schema.
	 */
	private final static String url = "jdbc:mysql://localhost:3306/book_store";
	/*
	 * Database user
	 */
	private final static String user = "java_user";
	/*
	 * Database user's password
	 */
	private final static String password = "javaJAVA$$";
	
	private DatabaseConnection() {}
	
	public static synchronized Connection getConnection(){
		try {
			if(connection == null || connection.isClosed()) {
				try {
					connection = DriverManager.getConnection(url, user, password);
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}	
}
