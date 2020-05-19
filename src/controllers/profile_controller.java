package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utils.User;

public class profile_controller implements Initializable, DelegateUser {

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

	private final String EDIT_PROFILE_URL = "/fxml/edit_profile.fxml", HOME_URL = "/fxml/Home.fxml";

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
			UtilControl.changeScene(event, getClass().getResource(EDIT_PROFILE_URL), user);
		} else if (event.getSource() == homeBt) {
			UtilControl.changeScene(event, getClass().getResource(HOME_URL), user);
		}
	}

	public void setUser(User user) {
		if (user != null) {
			this.user = user;
			user_name.setText(user.getUsername());
			first_name.setText(user.getFirstname());
			last_name.setText(user.getLastname());
			Email.setText(user.getEmail());
			gender.setText(user.getGender());
			Birth_date.setText(user.getBirthdate());
		}
	}
}
