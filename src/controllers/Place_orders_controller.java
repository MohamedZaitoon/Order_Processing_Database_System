package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.ConnectionUtil;
import utils.StatusUtil;
import utils.User;

public class Place_orders_controller implements Initializable {

    @FXML
    private AnchorPane addBt;

    @FXML
    private TextField isbn_feild;

    @FXML
    private TextField quantity_feild;

    @FXML
    private Button homeBt;

    @FXML
    private Label error;

private User user;
    
    private final String HOME_URL = "../fxml/Home.fxml";
    
    private Connection connection = null;
    private PreparedStatement preparedStatement = null;
    private CallableStatement callableStatement = null;
    private ResultSet resultSet = null;
    
    public Place_orders_controller () {
    	connection = ConnectionUtil.connectDatabase();
    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		StatusUtil.notifyConnectionStatus(connection, error);
	}

	@FXML
    public void handleButtonAction(ActionEvent event) {
        if (event.getSource() == addBt) {
        	add_order();
        } else if (event.getSource() == homeBt) {
        	changeScene(event, HOME_URL);
        }
    }

	private void add_order() {
		String isbn = isbn_feild.getText(), quantity = quantity_feild.getText();
		if (isbn.isEmpty() || quantity.isEmpty())
			StatusUtil.setLblError(error, Color.TOMATO, "Book ISBN or quantity not entered.");
		else {
			String sql = "SELECT * FROM orders WHERE ISBN = ?";
	        try {
	            preparedStatement = connection.prepareStatement(sql);
	            preparedStatement.setString(1, isbn);
	            resultSet = preparedStatement.executeQuery();
	            if (!resultSet.next()) {
	            	StatusUtil.setLblError(error, Color.TOMATO, "Book is not exist!");
	            } else {
	            	sql = "{ call place_oreder (?, ?) }";
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

	private void changeScene(ActionEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            HomeController controller = new HomeController();
            controller.setUser(user);
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
