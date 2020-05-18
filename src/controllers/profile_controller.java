package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    private Label Birth_date;

    @FXML
    private Button editBt;

    @FXML
    private Button homeBt;

    
    private User user; 
    
    private final String EDIT_PROFILE_URL = "../fxml/edit_profile.fxml",
    					 HOME_URL = "../fxml/Home.fxml";
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		/*
		 * user_name.setText(user.getUsername());
		 * first_name.setText(user.getFirstname());
		 * last_name.setText(user.getLastname()); Email.setText(user.getEmail());
		 * gender.setText(user.getGender()); Birth_date.setText(user.getBirthdate());
		 */
	}

    public profile_controller() {
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == editBt) {
        	changeScene(event, EDIT_PROFILE_URL);
        } else if (event.getSource() == homeBt) {
        	changeScene(event, HOME_URL);
        }
    }

    private void changeScene(ActionEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            
            if (event.getSource() == editBt) {
            	Edit_profile_controller controller = new Edit_profile_controller();
            	controller.setUser(user);
            } else if (event.getSource() == homeBt) {
            	////h
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
