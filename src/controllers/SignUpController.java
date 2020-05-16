package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;


public class SignUpController implements Initializable {
	
	@FXML
    private JFXTextField txtFirstname;
	
	@FXML
    private JFXTextField txtLastname;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtUsername;
    
    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtPasswordConfirm;

    @FXML
    private JFXComboBox<String> cobxGender;

    @FXML
    private DatePicker dpkbirthdate;
    
    @FXML
    private JFXButton btnResetAll;
    
    @FXML
    private JFXButton btnContinue;
    
    @FXML
    private Label lblErrors;
    
    @FXML
    private Label lblEmail;

    @FXML
    private Label lblUsername;
    
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;
    
    public SignUpController() {
    	connection = ConnectionUtil.connectDatabase();
    }
    
    private void setLblError(Label label, Color color, String text) {
        label.setTextFill(color);
        label.setText(text);
        System.out.println(text);
    }
    
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource().equals(btnContinue)) {
            if (signUp().equals("Success")) {
                changeScene(event, "../fxml/Home.fxml");
            }
        } else if (event.getSource().equals(btnResetAll)) {
        	resetAll();
        }
    }
    
    private void resetAll() {
    	txtFirstname.setText("");
    	txtLastname.setText("");
    	txtEmail.setText("");
    	txtUsername.setText("");
    	txtPassword.setText("");
    	txtPasswordConfirm.setText("");
    	cobxGender.getSelectionModel().clearSelection();
    	dpkbirthdate.getEditor().clear();
    	resetErrors();
    	checkConnection();
    }
    
    private void changeScene(MouseEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            
            if (event.getSource() == btnContinue) {
            	HomeController controller = new HomeController();
            	controller.setUserEmail(txtEmail.getText());
            }
            
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void resetErrors() {
    	lblErrors.setText("");
    	lblEmail.setText("");
    	lblUsername.setText("");
    }
    
    private String signUp() {
        String status = "Success";
        String firstname = txtFirstname.getText();
        String lastname = txtLastname.getText();
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String password = txtPassword.getText();
        String passwordConfirm = txtPasswordConfirm.getText();
        String gender = cobxGender.getValue();
        LocalDate birthdate = dpkbirthdate.getValue();
        resetErrors();
        
        if(firstname.isEmpty() || lastname.isEmpty() || email.isEmpty()
        		|| username.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()
        		|| gender == null || birthdate == null) {
            setLblError(lblErrors, Color.TOMATO, "Empty credentials");
            status = "Error";
        } else {
        	if (!password.equals(passwordConfirm)) {
        		setLblError(lblErrors, Color.TOMATO, "Invalid Password Confirmation");
                status = "Error";
        	}
            try {
            	String findEmail = "SELECT * FROM users WHERE email = ?";
            	preparedStatement = connection.prepareStatement(findEmail);
            	preparedStatement.setString(1, email);
            	resultSet = preparedStatement.executeQuery();
            	if (resultSet.next()) {
                    setLblError(lblEmail, Color.TOMATO, "Email already exists");
                    status = "Error";
                } 
            	
            	String findUsername = "SELECT * FROM users WHERE username = ?";
            	preparedStatement = connection.prepareStatement(findUsername);
            	preparedStatement.setString(1, username);
            	resultSet = preparedStatement.executeQuery();
            	if (resultSet.next()) {
                    setLblError(lblUsername, Color.TOMATO, "Username already exists");
                    status = "Error";
                }
            	
            	if (status == "Success") {
            		String sql = "INSERT INTO users VALUES(NULL, ?, ?, ?, ? , ? , ?, ?, ?)";
            		preparedStatement = connection.prepareStatement(sql);
                	preparedStatement.setString(1, email);
                	preparedStatement.setString(2, firstname);
                	preparedStatement.setString(3, lastname);
                	preparedStatement.setString(4, username);
                	preparedStatement.setString(5, birthdate.toString());
                	preparedStatement.setString(6, "Customer");
                	preparedStatement.setString(7, mapGender(gender));
                	preparedStatement.setString(8, password);
                	preparedStatement.executeUpdate();
            	}
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
            }
        }
        
        return status;
    }
    
    private String mapGender(String gender) {
    	String res = "";
    	switch (gender) {
    		case "Male":    return res = "M";
    		case "Female" : return res = "F";
    		case "Other"  : return res = "U";
    	}
    	return res;
    }
    
    private void checkConnection() {
    	if (connection == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Server Connection Failure");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Connected to Server");
        }
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cobxGender.getItems().addAll("Male", "Female", "Other");
		checkConnection();
	}

}
