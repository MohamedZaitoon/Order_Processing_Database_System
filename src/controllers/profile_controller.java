package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import utils.ConnectionUtil;
import utils.StatusUtil;
import utils.User;
import utils.StatusUtil.Status;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class profile_controller implements Initializable {

    @FXML
    private Label user_name;

    @FXML
    private Label first_name;

    @FXML
    private Label last_name;

    @FXML
    private Label Email;

    @FXML
    private Label gender;

    @FXML
    private Button editBt;

    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    private User user; 
    
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
    
    
    
    
    
    public void setUser(User user) {
    	if (user != null)
    		this.user = user;
    }
}
