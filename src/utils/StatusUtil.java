package utils;

import java.sql.Connection;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class StatusUtil {
	
	public static enum Status {SUCCESS, ERROR, EXCEPTION};
	public static String CUSTOMER = "Customer";
	public static String MANAGER = "Manager";
	
	public static void notifyConnectionStatus(Connection connection, Label label) {
    	if (connection == null) {
            label.setTextFill(Color.TOMATO);
            label.setText("Server Connection Failure");
        } else {
            label.setTextFill(Color.GREEN);
            label.setText("Connected to Server");
        }
    }
	
	public static void setLblError(Label label, Color color, String text) {
        label.setTextFill(color);
        label.setText(text);
        System.out.println(text);
    }
	 
}
