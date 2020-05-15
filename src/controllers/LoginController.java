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
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;

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
        if (event.getSource() == btnSignin) {
            if (logIn().equals("Success")) {
                changeScene(event, "../fxml/Home.fxml");
            }
        } else if (event.getSource() == btnSignup) {
        	changeScene(event, "../fxml/SignUp.fxml");
        }
    }
    
    private void changeScene(MouseEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = FXMLLoader.load(getClass().getResource(path));
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(path)));
            
            if (event.getSource() == btnSignin) {
            	HomeController controller = fxmlLoader.<HomeController>getController();
            	controller.setUserInfo(resultSet);
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
        if (connection == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Connection Failure");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Connected to Server");
        }
    }
    
    public LoginController() {
        connection = ConnectionUtil.connectDatabase();
    }
    
    private String logIn() {
        String status = "Success";
        String email = txtUsername.getText();
        String password = txtPassword.getText();
        if(email.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Empty credentials");
            status = "Error";
        } else {
            String sql = "SELECT * FROM users WHERE email = ? AND user_password = ?";
            try {
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Enter Correct Email/Password");
                    status = "Error";
                } else {
                    setLblError(Color.GREEN, "Login Successful..Redirecting..");
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        
        return status;
    }
    
    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}
