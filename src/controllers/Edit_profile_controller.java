package controllers;

import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import model.DatabaseConnection;
import utils.StatusUtil;
import utils.StatusUtil.Status;
import utils.User;

public class Edit_profile_controller implements Initializable,DelegateUser {

    @FXML
    private TextField user_name_feild;

    @FXML
    private TextField first_name_feild;

    @FXML
    private TextField last_name_feild;

    @FXML
    private DatePicker birth_date;

    @FXML
    private ChoiceBox<String> gender_choice_box;

    @FXML
    private Button discard;

    @FXML
    private Button confirm;

    @FXML
    private Label error;
    
    ObservableList<String> genderList  = 
    		FXCollections.observableArrayList("Male", "Female");
    private User user;
    private final String PROFILE_URL = "../fxml/profile.fxml";
    
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private CallableStatement callableStatement = null;
    private ResultSet resultSet = null;
    
    public Edit_profile_controller() {
    	connection = DatabaseConnection.getConnection();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
    	gender_choice_box.setItems(genderList);
    	StatusUtil.notifyConnectionStatus(connection, error);
    }
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == discard) {
        	UtilControl.changeScene(event, getClass().getResource(PROFILE_URL), user);
        } else if (event.getSource() == confirm && updating() == Status.SUCCESS) {
        	UtilControl.changeScene(event, getClass().getResource(PROFILE_URL), user);
        }
    }

	public void setUser(User user) {
		this.user = user;
	}

	private Status updating() {
		Status status = Status.SUCCESS;
		String userName = user_name_feild.getText(),
				firstName = first_name_feild.getText(),
				lastName = last_name_feild.getText(),
				gender = gender_choice_box.getValue(),
				birthDate = "";
		if (birth_date.getValue() != null)
			birthDate = birth_date.getValue().toString();
		if(userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty()
				|| gender.isEmpty() || birthDate.isEmpty()) {
        	StatusUtil.setLblError(error, Color.TOMATO, "Empty credentials");
            status = Status.ERROR;
        } else {
        	
        	
	    	String sql = "SELECT * FROM users WHERE username = ?";
	        try {
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, userName);
	            resultSet = preparedStatement.executeQuery();
	            if (resultSet.next() && resultSet.getString(5) != user.getUsername()) {
	            	StatusUtil.setLblError(error, Color.TOMATO, "New UserName is already token");
	                status = Status.ERROR;
	            } else {
	            	sql = "{ CALL edit_personal_info (?, ?, ?, ?, ?, ?, ?, ?) }";
	            	callableStatement = connection.prepareCall(sql);
	            	callableStatement.setInt(1, resultSet.getInt(1));
	            	callableStatement.setString(2, firstName);
	            	callableStatement.setString(3, lastName);
	            	callableStatement.setString(4, resultSet.getString(2));
	            	callableStatement.setString(5, userName);
	            	callableStatement.setString(6, birthDate);
	            	callableStatement.setString(7, gender);
	            	callableStatement.setString(8, resultSet.getString(9));
	            	callableStatement.execute();
	                StatusUtil.setLblError(error, Color.GREEN, "Update completed");
	       
	                user.setUserInfo(user.getEmail(), firstName, lastName, userName,
	                		birthDate,user.getUserRole() ,gender, user.getPassword());
	            }
	        } catch (SQLException ex) {
	            System.err.println(ex.getMessage());
	            status = Status.EXCEPTION;
	        }
        }
		return status;
	}

}
