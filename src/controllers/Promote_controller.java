package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.DatabaseConnection;
import utils.ConnectionUtil;
import utils.StatusUtil;
import utils.User;
import utils.StatusUtil.Status;

public class Promote_controller implements Initializable {

    @FXML
    private TextField uesr_name_feild;

    @FXML
    private Button promoteBt;

    @FXML
    private Label error;

    @FXML
    private Button homeBt;
    
    private User user;
    
    private final String HOME_URL = "../view/ManagerPage.fxml";
    
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
	}

	@FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == promoteBt) {
        	promote();
        } else if (event.getSource() == homeBt) {
        	changeScene(event, HOME_URL);
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

	private void changeScene(ActionEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
//            HomeController controller = new HomeController();
//            controller.setUser(user);
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

	public void setUser(User user) {
		this.user = user;
	}
}
