package controllers;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.DatabaseConnection;
import utils.StatusUtil;

public class Place_orders_controller implements Initializable {

    @FXML
    private Button addBt;

    @FXML
    private TextField isbn_feild;

    @FXML
    private TextField quantity_feild;

    @FXML
    private Button homeBt;

    @FXML
    private Label error;

    
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private CallableStatement callableStatement = null;
    private ResultSet resultSet = null;
    
    public Place_orders_controller () {
    	connection = DatabaseConnection.getConnection();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		StatusUtil.notifyConnectionStatus(connection, error);
		
		
		UtilControl.addValidation(isbn_feild, UtilControl.DIGRGX, UtilControl.NDIGRGX);
		UtilControl.addValidation(quantity_feild, UtilControl.DIGRGX, UtilControl.NDIGRGX);
		
		// set limitation length of each text field
		UtilControl.addTextLimiter(isbn_feild, 13);
		UtilControl.addTextLimiter(quantity_feild, UtilControl.MAXINTLENGHT);
	}

	@FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == addBt) {
        	add_order();
        }
    }

	private void add_order() {
		String isbn = isbn_feild.getText(), quantity = quantity_feild.getText();
		if (isbn.isEmpty() || quantity.isEmpty())
			StatusUtil.setLblError(error, Color.TOMATO, "Book ISBN or quantity not entered.");
		else {
			String sql = "SELECT * FROM book WHERE ISBN = ?";
	        try {
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, isbn);
	            resultSet = preparedStatement.executeQuery();
	            if (!resultSet.next()) {
	            	StatusUtil.setLblError(error, Color.TOMATO, "Book is not exist!");
	            } else {
	            	sql = "{ call place_order (?, ?) }";
	            	callableStatement = connection.prepareCall(sql);
	            	callableStatement.setString(1, isbn);
	            	callableStatement.setInt(2, Integer.parseInt(quantity));
	            	callableStatement.execute(); 	
	                StatusUtil.setLblError(error, Color.GREEN, "Order has been inserted.");
	            }
	        } catch (SQLException ex) {
	            System.err.println(ex.getMessage());
	        }
		}
	}


}
