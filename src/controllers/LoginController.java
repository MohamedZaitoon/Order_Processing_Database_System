package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DatabaseConnection;
import utils.StatusUtil;
import utils.StatusUtil.Status;
import utils.User;

public class LoginController implements Initializable {
	
	@FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;
    
    @FXML
    private Button btnSignup;
    
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignin && logIn().equals(Status.SUCCESS)) {
        	changeScene(event, StatusUtil.HOME_URL);
        } else if (event.getSource() == btnSignup) {
        	changeScene(event, StatusUtil.SIGNUP_URL);
        }
    }
    
    private void changeScene(MouseEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            
            if (event.getSource() == btnSignin) {
            	HomeController controller = fxmlLoader.getController();
            	controller.sendUserInfo(getUserInfo(resultSet));
            }
            
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        StatusUtil.notifyConnectionStatus(connection, lblErrors);
    }
    
    public LoginController() {
        connection = DatabaseConnection.getConnection();
    }
    
    private Status logIn() {
        Status status = Status.SUCCESS;
        String email = txtUsername.getText();
        String password = txtPassword.getText();
        if(email.isEmpty() || password.isEmpty()) {
        	StatusUtil.setLblError(lblErrors, Color.TOMATO, "Empty credentials");
            status = Status.ERROR;
        } else {
            String sql = "SELECT * FROM users WHERE email = ? AND user_password = ?";
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                	StatusUtil.setLblError(lblErrors, Color.TOMATO, "Enter Correct Email/Password");
                    status = Status.ERROR;
                } else {
                    StatusUtil.setLblError(lblErrors, Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = Status.EXCEPTION;
            }
        }
        
        return status;
    }
    
    private User getUserInfo(ResultSet rs) {
    	try {
    		User user = new User();
    		user.setUserInfo(rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
        			rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9));
    		return user;
    	} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
