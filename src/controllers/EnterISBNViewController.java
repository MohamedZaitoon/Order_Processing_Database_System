package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import model.BookController;
import model.OperationResponse;
import utils.User;

public class EnterISBNViewController implements Initializable, DelegateUser{
	@FXML
	private AnchorPane editInputLayout;
	@FXML
	private TextField isbnTxt;
	@FXML
	private Button goBtn;

	private StackPane mainView;
	
	private BookController bc;
	
	@SuppressWarnings("unused")
	private User user;
	
	public void setParentView(StackPane _mainView) {
		mainView = _mainView;
	}
	// Event Listener on TextField[#isbnTxt].onAction
	@FXML
	public void goEditBookListener(ActionEvent event) {
		String isbn = isbnTxt.getText().trim();
		if (!bc.isConnected()) {
			// TODO check server connected
			System.out.println("server faild");
			return;
		}
		if (isbn.isEmpty()) {
			// TODO : label for error
			System.out.println("Empty field");
			return;
		}

		if (isbn.length() != bc.getISBNMaxLength()) {
			System.out.println("Invalid ISBN");
			return;
		}
		OperationResponse or = bc.searchForBook(isbn);
		if (!or.isExecutedSuccessfuly()) {
			System.out.println("Invalid ISBN");
			return;
		}
		try {
			clearControls();
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/ModifyBook.fxml"));
			Parent root = fxmlLoader.load();
			ModifyBookViewController controller = fxmlLoader.getController();
			controller.setISBN(isbn);
			controller.setParent("/fxml/ManagerPage.fxml");
			mainView.getChildren().setAll(root);

		} catch (IOException ex) {
			System.err.println(ex.getMessage());
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bc = new BookController();
		// Edit layout controls init
		UtilControl.addValidation(isbnTxt, UtilControl.DIGRGX, UtilControl.NDIGRGX);
		UtilControl.addTextLimiter(isbnTxt, bc.getISBNMaxLength());
	}

	private void clearControls() {
		isbnTxt.setText("");
	}
	
	@Override
	public void setUser(User user) {
		this.user = user;
	}
	
	
}
