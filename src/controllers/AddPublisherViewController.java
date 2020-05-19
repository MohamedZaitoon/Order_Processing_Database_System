package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.OperationResponse;
import model.Publisher;
import model.PublisherController;

public class AddPublisherViewController implements Initializable {
	@FXML
	private AnchorPane addPublisherLayout;
	@FXML
	private TextField publisherNameTxt;
	@FXML
	private TextField addressTxt;
	@FXML
	private TextField phoneTxt;
	@FXML
	private Label errorLbl;

	private PublisherController pc;
	
	// Event Listener on Button.onAction
	@FXML
	public void addPublisherListener(ActionEvent event) {
		errorLbl.setText("");
		if(!pc.isConnected()) {
			errorLbl.setText("Server is disconnected");
		}
		
		Publisher publisher = getPublisher();
		if(publisher != null) {
			OperationResponse or = pc.addPublisher(publisher);
			if(!or.isExecutedSuccessfuly()) {
				errorLbl.setText(or.getErrorMessage());
			}else {
				clearPublisherControls();
			}
		}
	}

	
	private Publisher getPublisher() {
		Publisher publisher;
		String name = publisherNameTxt.getText().trim();
		String address = addressTxt.getText().trim();
		String phone = phoneTxt.getText().trim();
		
		if(name.isEmpty() || address.isEmpty() || phone.isEmpty()) {
			errorLbl.setText("Some fields are empty");
			return null;
		}
		
		publisher = new Publisher(name, address, phone);
		return publisher;
	}
	
	private void clearPublisherControls() {
		errorLbl.setText("");
		publisherNameTxt.setText("");
		addressTxt.setText("");
		phoneTxt.setText("");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		pc = new PublisherController();
		// Add publisher controls init
				UtilControl.addValidation(publisherNameTxt, UtilControl.WORDRGX, UtilControl.NWORDRGX);
				UtilControl.addTextLimiter(publisherNameTxt, pc.getNameMaxLenth());
				UtilControl.addValidation(addressTxt, UtilControl.WORDRGX, UtilControl.NWORDRGX);
				UtilControl.addTextLimiter(addressTxt, pc.getAddressMaxLenth());
				UtilControl.addValidation(phoneTxt, UtilControl.DIGRGX, UtilControl.NDIGRGX);
				UtilControl.addTextLimiter(phoneTxt, pc.getPhoneMaxLenth());
		
	}
}
