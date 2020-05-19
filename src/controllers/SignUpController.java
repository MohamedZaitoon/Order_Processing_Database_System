package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import utils.StatusUtil;
import utils.StatusUtil.Status;
import utils.User;

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
    private User user;
    
    public SignUpController() {
    	user = new User();
    	connection = ConnectionUtil.connectDatabase();
    }
    
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource().equals(btnContinue) && signUp().equals(Status.SUCCESS)) {
        	changeScene(event, StatusUtil.HOME_URL);
        } else if (event.getSource().equals(btnResetAll)) {
        	resetAll();
        }
    }
    
    private void changeScene(MouseEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            
            if (event.getSource() == btnContinue) {
            	HomeController controller = fxmlLoader.getController();
            	controller.sendUserInfo(user);
            }
            
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
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
    	StatusUtil.notifyConnectionStatus(connection, lblErrors);
    }
    
    private void resetErrors() {
    	lblErrors.setText("");
    	lblEmail.setText("");
    	lblUsername.setText("");
    }
    
    private Status signUp() {
    	Status status = Status.SUCCESS;
    	setSignUpInfo();
    	String passwordConfirm = txtPasswordConfirm.getText();
        resetErrors();
        
        if(user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getEmail().isEmpty()
        		|| user.getUsername().isEmpty() || user.getPassword().isEmpty()
        		|| passwordConfirm.isEmpty() || user.getGender() == null
        		|| user.getBirthdate() == null) {
            StatusUtil.setLblError(lblErrors, Color.TOMATO, "Empty credentials");
            status = Status.ERROR;
        } else {
        	System.out.println(user.getPassword());
        	System.out.println(passwordConfirm);
        	System.out.println(!user.getPassword().equals(passwordConfirm));
        	if (!user.getPassword().equals(passwordConfirm)) {
        		StatusUtil.setLblError(lblErrors, Color.TOMATO, "Invalid Password Confirmation");
                status = Status.ERROR;
        	}
        	
        	status = checkEmailExistence(status, user.getEmail());
        	status = checkUsernameExistence(status, user.getUsername());
        	
        	if (status != Status.ERROR) {
        		status = InsertUser(user);
        	}
        }
        
        return status;
    }
    
    private void setSignUpInfo() {
    	user.setEmail(txtEmail.getText());
    	user.setFirstname(txtFirstname.getText());
    	user.setLastname(txtLastname.getText());
    	user.setUsername(txtUsername.getText());
    	user.setPassword(txtPassword.getText());
    	user.setGender(cobxGender.getValue());
    	user.setBirthdate(dpkbirthdate.getValue().toString());
    }
    
    private Status checkEmailExistence(Status prevStatus,String email) {
    	try {
    		String sql = "SELECT * FROM users WHERE email = ?";
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setString(1, email);
        	resultSet = preparedStatement.executeQuery();
        	if (resultSet.next()) {
        		StatusUtil.setLblError(lblEmail, Color.TOMATO, "Email already exists");
        		return Status.ERROR;
            }
        	return prevStatus;
    	} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return Status.EXCEPTION;
        }
    }
    
    private Status checkUsernameExistence(Status prevStatus, String username) {
    	try {
    		String sql = "SELECT * FROM users WHERE username = ?";
        	preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setString(1, username);
        	resultSet = preparedStatement.executeQuery();
        	if (resultSet.next()) {
        		StatusUtil.setLblError(lblUsername, Color.TOMATO, "Username already exists");
        		return Status.ERROR;
            }
        	return prevStatus;
    	} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return Status.EXCEPTION;
        }
    }
    
    private Status InsertUser(User user) {
    	try {
    		String sql = "INSERT INTO users VALUES(NULL, ?, ?, ?, ? , ? , ?, ?, ?)";
    		preparedStatement = connection.prepareStatement(sql);
        	preparedStatement.setString(1, user.getEmail());
        	preparedStatement.setString(2, user.getFirstname());
        	preparedStatement.setString(3, user.getLastname());
        	preparedStatement.setString(4, user.getUsername());
        	preparedStatement.setString(5, user.getBirthdate().toString());
        	preparedStatement.setString(6, StatusUtil.CUSTOMER);
        	preparedStatement.setString(7, User.mapGender(user.getGender()));
        	preparedStatement.setString(8, user.getPassword());
        	preparedStatement.executeUpdate();
        	return Status.SUCCESS;
    	} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return Status.EXCEPTION;
        }
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cobxGender.getItems().addAll("Male", "Female", "Other");
		StatusUtil.notifyConnectionStatus(connection, lblErrors);
	}

}
