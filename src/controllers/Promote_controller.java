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

public class Promote_controller implements Initializable {

    @FXML
    private TextField uesr_name_feild;

    @FXML
    private Button promoteBt;

    @FXML
    private Label error;

    @FXML
    private Button homeBt;
    
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private CallableStatement callableStatement = null;
    private ResultSet resultSet = null;
    
    public Promote_controller () {
    	connection = DatabaseConnection.getConnection();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		StatusUtil.notifyConnectionStatus(connection, error);
		UtilControl.addValidation(uesr_name_feild, UtilControl.WORDRGX, UtilControl.NWORDRGX);

		// set limitation length of each text field
		UtilControl.addTextLimiter(uesr_name_feild, 60);
	}

	@FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == promoteBt) {
        	promote();
        }
    }

	private void promote() {
		String userName = uesr_name_feild.getText();
		if (userName.isEmpty())
			StatusUtil.setLblError(error, Color.TOMATO, "Enter user_name!");
		else {
			String sql = "SELECT * FROM users WHERE username = ?";
	        try {
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, userName);
	            resultSet = preparedStatement.executeQuery();
	            if (!resultSet.next()) {
	            	StatusUtil.setLblError(error, Color.TOMATO, "UserName is not exist!");
	            } else {
	            	sql = "{ call promote (?) }";
	            	callableStatement = connection.prepareCall(sql);
	            	callableStatement.setInt(1, resultSet.getInt(1));
	            	callableStatement.execute(); 	
	                StatusUtil.setLblError(error, Color.GREEN, "Promotion is done.");
	            }
	        } catch (SQLException ex) {
	            System.err.println(ex.getMessage());
	        }
		}
	}
}
