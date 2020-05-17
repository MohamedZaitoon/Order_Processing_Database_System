package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.fxml.Initializable;
import utils.User;
import java.io.IOException;
import java.net.URL;
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

    
    private User user; 
    
    private final String EDIT_PROFILE_URL = "../fxml/edit_profile.fxml";
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
    @FXML
    public void handleButtonAction(MouseEvent event) {
        if (event.getSource() == editBt) {
        	changeScene(event, EDIT_PROFILE_URL);
        }
    }

    private void changeScene(MouseEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            
            if (event.getSource() == editBt) {
            	Edit_profile_controller controller = new Edit_profile_controller();
            	controller.setUser(user);
            }
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void setUser(User user) {
    	if (user != null)
    		this.user = user;
    }
}
