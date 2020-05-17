package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import utils.User;

public class Edit_profile_controller {

    @FXML
    private TextField user_name_feild;

    @FXML
    private TextField first_name_feild;

    @FXML
    private TextField last_name_feild;

    @FXML
    private Button discard;

    @FXML
    private Button confirm;

    @FXML
    private Label error;
    
    
    private User user;
    
    public void Edit_profile_controller() {
    }

	public void setUser(User user) {
		this.user = user;
	}

}
