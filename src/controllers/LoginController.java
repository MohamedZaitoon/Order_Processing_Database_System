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
import utils.ConnectionUtil;
import utils.StatusUtil;
import utils.User;
import utils.StatusUtil.Status;

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
    
    private final String HOME_URL = "../fxml/Home.fxml";
    private final String SIGNUP_URL = "../fxml/SignUp.fxml";
    
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == btnSignin && logIn().equals(Status.SUCCESS)) {
        	changeScene(event, HOME_URL);
        } else if (event.getSource() == btnSignup) {
        	changeScene(event, SIGNUP_URL);
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
            	HomeController controller = new HomeController();
            	controller.setUser(getUserInfo(resultSet));
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
        connection = ConnectionUtil.connectDatabase();
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
        			rs.getString(6), rs.getString(7), rs.getString(8));
    		return user;
    	} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
    }
}
