package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.Book;
import model.BookController;
import model.OperationResponse;
import utils.User;

public class ModifyBookViewController implements Initializable, DelegateUser {

	private String parent = "";
	@FXML
	private TextField isbnTxt;
	@FXML
	private TextField titleTxt;
	@FXML
	private TextField noCopiesTxt;
	@FXML
	private TextField thresholdTxt;
	@FXML
	private TextField sellingPriceTxt;
	@FXML
	private TextField publishertxt;
	@FXML
	private ComboBox<String> categoryComBx;
	@FXML
	private ComboBox<Integer> yearComBx;

	@FXML
	private Label errorLbl;
	@FXML
	private TextField authorTxt;
	@FXML
	private ListView<String> authorList;
	@FXML
	private Button addToAuthBtn;
	@FXML
	private Button deleteAuthorBtn;

	private String ISBN;

	private ArrayList<String> authors;

	private ArrayList<String> deletedAuthors;

	private ArrayList<String> addedAuthors;

	public void setISBN(String _ISBN) {
		ISBN = _ISBN;
		setInfoBook();
	}
	public void setParent(String p) {
		parent = p;
	}
	
	/*
	 * Apply operation on book with database
	 */
	private BookController bookController;
	private User user;

	@FXML
	public void cancelListener(ActionEvent event) {
		clearFields();
		UtilControl.changeScene(event, getClass().getResource(parent), user);
	}

	@FXML
	public void saveListener(ActionEvent event) {
		if (!bookController.isConnected()) {
			errorLbl.setText("Server is disconnected");
			return;
		}

		Book book = getBook();
		if (book == null)
			return;

		OperationResponse or = bookController.updateBook(book);

		if (!or.isExecutedSuccessfuly()) {
			errorLbl.setText(or.getErrorMessage());
			return;
		}

		if (!deletedAuthors.isEmpty()) {
			or = bookController.deleteAuthors(book.getISBN(), deletedAuthors);
			if (!or.isExecutedSuccessfuly()) {
				errorLbl.setText(or.getErrorMessage());
				return;
			}
		}

		if (!addedAuthors.isEmpty()) {
			or = bookController.addAuthors(book.getISBN(), addedAuthors);
			if (!or.isExecutedSuccessfuly()) {
				errorLbl.setText(or.getErrorMessage());
				return;
			}
		}	

		clearFields();
		UtilControl.changeScene(event, getClass().getResource(parent),user);
	}

	@FXML
	public void addAuthorListener(ActionEvent event) {
		String authorName = authorTxt.getText().trim();
		if (!authorName.isEmpty() && !authors.contains(authorName)) {
			addedAuthors.add(authorName);
			authorList.getItems().add(authorName);
			authorTxt.setText("");
		}
	}

	@FXML
	public void deleteSelectedAuthorListener(ActionEvent event) {
		final int selectedIdx = authorList.getSelectionModel().getSelectedIndex();
		if (selectedIdx != -1) {
			String itemToRemove = authorList.getSelectionModel().getSelectedItem();

			final int newSelectedIdx = (selectedIdx == authorList.getItems().size() - 1) ? selectedIdx - 1
					: selectedIdx;

			authorList.getItems().remove(selectedIdx);
			authorList.getSelectionModel().select(newSelectedIdx);
			// removes the author for the array
			System.out.println("selectIdx: " + selectedIdx);
			System.out.println("item: " + itemToRemove);
			if (!addedAuthors.contains(itemToRemove)) {
				authors.remove(selectedIdx);
				deletedAuthors.add(itemToRemove);
			}
		}
	}

	@FXML
	void resetListener(ActionEvent event) {
		setInfoBook();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		bookController = new BookController();
		authors = new ArrayList<>();
		deletedAuthors = new ArrayList<>();
		addedAuthors = new ArrayList<>();
		if (bookController.isConnected()) {
			// set validation input to text field
			UtilControl.addValidation(noCopiesTxt, UtilControl.DIGRGX, UtilControl.NDIGRGX);
			UtilControl.addValidation(thresholdTxt, UtilControl.DIGRGX, UtilControl.NDIGRGX);
			UtilControl.addValidation(isbnTxt, UtilControl.DIGRGX, UtilControl.NDIGRGX);
			UtilControl.addValidation(sellingPriceTxt, UtilControl.DIGRGX, UtilControl.NDIGRGX);
			UtilControl.addValidation(authorTxt, UtilControl.WORDRGX, UtilControl.NWORDRGX);

			// set limitation length of each text field
			UtilControl.addTextLimiter(isbnTxt, bookController.getISBNMaxLength());
			UtilControl.addTextLimiter(titleTxt, bookController.getTitleMaxLenth());
			UtilControl.addTextLimiter(publishertxt, bookController.getPublisherMaxLength());
			UtilControl.addTextLimiter(authorTxt, bookController.getAuthorMaxLength());
			UtilControl.addTextLimiter(noCopiesTxt, UtilControl.MAXINTLENGHT);
			UtilControl.addTextLimiter(thresholdTxt, UtilControl.MAXINTLENGHT);
			UtilControl.addTextLimiter(sellingPriceTxt, UtilControl.MAXINTLENGHT);

			// initiate combo boxes
			UtilControl.initCatigories(categoryComBx);
			UtilControl.initYears(yearComBx);

//			setDisableFields(true);
//			errorLbl.setText("Press rest For first time");
		} else {
			errorLbl.setText("Server is disconnected");
		}
	}

	private void setInfoBook() {
		if (ISBN == null || ISBN.isEmpty()) {
			errorLbl.setText("ISBN not defined yet");
			setDisableFields(true);
			return;
		}
		setDisableFields(false);

		OperationResponse or = bookController.searchForBook(ISBN);
		if (!or.isExecutedSuccessfuly()) {
			errorLbl.setText(or.getErrorMessage());
			return;
		}
		errorLbl.setText("");

		Book book = bookController.getBookInstance(or.getResultSet());

		isbnTxt.setText(book.getISBN());
		titleTxt.setText(book.getTitle());
		noCopiesTxt.setText(book.getNoCopies().toString());
		thresholdTxt.setText(book.getThreshold().toString());
		sellingPriceTxt.setText(book.getSellingPrice().toString());
		publishertxt.setText(book.getPublisher());

		categoryComBx.setValue(book.getCategory());
		yearComBx.setValue(Integer.parseInt(book.getPublicationYear().substring(0, 4)));

		authors = new ArrayList<>();
		deletedAuthors = new ArrayList<>();
		addedAuthors = new ArrayList<>();

		authors = bookController.getAuthorOfBook(ISBN);
		authorList.getItems().clear();
		authorList.getItems().addAll(authors);
	}

	private Book getBook() {
		Book newBook;
		String ISBN = isbnTxt.getText().trim();
		String title = titleTxt.getText().trim();
		String publisher = publishertxt.getText().trim();
		String category = categoryComBx.getValue();
		String _noCopies = noCopiesTxt.getText().trim();
		String _threshold = thresholdTxt.getText().trim();
		String _price = sellingPriceTxt.getText().trim();
		Integer year = yearComBx.getValue();

		if (ISBN.length() != bookController.getISBNMaxLength()) {
			errorLbl.setText("ISBN must be at 13 digits");
			return null;
		}

		if (ISBN.isEmpty() || title.isEmpty() || publisher.isEmpty() || category == null || _noCopies.isEmpty()
				|| _threshold.isEmpty() || _price.isEmpty() || year == null) {
			errorLbl.setText("Some fields are empty or not selected");
			return null;
		}

		Integer noCopies = Integer.parseInt(noCopiesTxt.getText().trim());
		Integer threshold = Integer.parseInt(_threshold);
		Integer sellingPrice = Integer.parseInt(_price);

		newBook = new Book(ISBN, title, category, sellingPrice, noCopies, threshold, publisher, year + "-1-1");

		return newBook;
	}

	/*
	 * Reset all controllers in scene.
	 */
	private void clearFields() {
		isbnTxt.setText("");
		titleTxt.setText("");
		publishertxt.setText("");
		authorTxt.setText("");
		noCopiesTxt.setText("");
		thresholdTxt.setText("");
		sellingPriceTxt.setText("");
		categoryComBx.getSelectionModel().clearSelection();
		yearComBx.getSelectionModel().clearSelection();

		authorList.getItems().clear();
		authors.clear();

		errorLbl.setText("");
	}

	private void setDisableFields(boolean bool) {
		isbnTxt.setDisable(bool);
		titleTxt.setDisable(bool);
		publishertxt.setDisable(bool);
		authorTxt.setDisable(bool);
		noCopiesTxt.setDisable(bool);
		thresholdTxt.setDisable(bool);
		sellingPriceTxt.setDisable(bool);
		categoryComBx.setDisable(bool);
		yearComBx.setDisable(bool);
		authorList.setDisable(bool);
		addToAuthBtn.setDisable(bool);
		deleteAuthorBtn.setDisable(bool);
	}
	@Override
	public void setUser(User user) {
		// TODO Auto-generated method stub
		this.user = user;
	}

}
