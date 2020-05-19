package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

public class ManagerPageViewController implements Initializable {
	private String parent = "/fxml/Home.fxml";

	@FXML
    private StackPane mainView;
	@FXML
	public void showReportsListener(ActionEvent event) {
		loadPaneInto("/fxml/Reports.fxml");
	}

	@FXML
	public void confirmOrderListener(ActionEvent event) {
		loadPaneInto("/fxml/Orders.fxml");
	}

	@FXML
	public void promoteListener(ActionEvent event) {
		loadPaneInto("/fxml/promote.fxml");
	}

	@FXML
	public void placeOrderListenr(ActionEvent event) {
		loadPaneInto("/fxml/place_orders.fxml");
	}

	@FXML
	public void addBookListener(ActionEvent event) {
		loadPaneInto("/fxml/AddBook.fxml");
	}

	@FXML
	public void editBookListener(ActionEvent event) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/EnterISBN.fxml"));
			Parent root = fxmlLoader.load();
			EnterISBNViewController controller = fxmlLoader.getController();
			controller.setParentView(mainView);
			mainView.getChildren().setAll(root);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}

    @FXML
    void showAddPublisherLayout(ActionEvent event) {
    	loadPaneInto("/fxml/AddPublisher.fxml");
    }
    
	@FXML
	void backListener(ActionEvent event) {
    	UtilControl.changeScene(event, getClass().getResource(parent));
	}

	public  void loadPaneInto(String path) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
			Parent root = fxmlLoader.load();
			mainView.getChildren().setAll(root);
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		loadPaneInto("/fxml/AddPublisher.fxml");
	}
}
