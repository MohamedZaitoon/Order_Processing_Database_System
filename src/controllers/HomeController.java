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
import utils.User;

public class HomeController implements Initializable {
	
	private User user;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public HomeController() {
		
	}
	
	public void setUser(User user) {
		if (user != null) {
			this.user = user;
		}
	}

}
