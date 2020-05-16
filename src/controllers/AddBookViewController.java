package controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import model.Book;
import model.BookController;
import model.OperationResponse;

public class AddBookViewController implements Initializable{
	/*
	 * No. years Available for publication year.
	 */
	private final int YEARS = 100;
	/*
	 * Max length for integer textfield
	 */
	private final int MAXINTLENGHT = 9;
	/*
	 * Regex that matches only digits.
	 */
	private String DIGRGX = "\\d*";
	private String NDIGRGX = "[^\\d]*";
	/*
	 * Regex that matches only word characters.
	 */
	private String WORDRGX = "[\\s\\w]*";
	private String NWORDRGX = "[^\\s\\w]";
	
	@FXML private TextField isbnTxt;
	
	@FXML private TextField titleTxt;
	
	@FXML private ComboBox<String> categoryComBx;
	
	@FXML private TextField noCopiesTxt;
	
	@FXML private TextField thresholdTxt;
	
	@FXML private TextField sellingPriceTxt;
	
	@FXML private TextField publishertxt;
	
	@FXML private ComboBox<Integer> yearComBx;
	
	@FXML private TextField authorTxt;
	
	@FXML private Button addToAuthBtn;
	
	@FXML private Button deleteAuthorBtn;
	@FXML private ListView<String> authorList;
	
	@FXML private Button addBookBtn;
	
	@FXML private Label errorLabel;

	@FXML private Label emptyFieldsLbl;
	
	private ArrayList<String> authors;
	
	/*
	 * Apply operation on book with database
	 */
	private BookController bookController;
	
	/*
	 * Adding name of author at authortxt text field to list of authors.
	 */
	@FXML
	public void addAuthorListener(Event event) {
		String authorName = authorTxt.getText().trim();
		if(!authorName.isEmpty()&&!authors.contains(authorName)) {
			authors.add(authorName);
			authorList.getItems().add(authorName);
			authorTxt.setText("");
		}
	}
	
	/*
	 * Adding book to database
	 */
	@FXML
	public void addBookListener(MouseEvent event) {
		if(!bookController.isConnected()) {
			errorLabel.setText("Server is disconnected");
			return;
		}
		
		Book newBook = getBook();
		if(newBook == null )
			return;
		if(authors.isEmpty()) {
			emptyFieldsLbl.setText("Authors' list is empty");
			return;
		}
		
		OperationResponse or = bookController.addBook(newBook);
		
		if(!or.isExecutedSuccessfuly()) {
			errorLabel.setText(or.getErrorMessage());
			return;
		}
		or = bookController.addAuthors(newBook.getISBN(), authors);
		
		if(!or.isExecutedSuccessfuly()) {
			errorLabel.setText(or.getErrorMessage());
			return;
		}
		
		clearFields();
	}
	
	@FXML
    void deleteSelectedAuthorListener(ActionEvent event) {
		 deleteAuthorBtn.setOnAction(new EventHandler<ActionEvent>() {
		        @Override
		        public void handle(ActionEvent event) {
		            final int selectedIdx = authorList.getSelectionModel().getSelectedIndex();
		            if (selectedIdx != -1) {
		                String itemToRemove = authorList.getSelectionModel().getSelectedItem();

		                final int newSelectedIdx =
		                        (selectedIdx == authorList.getItems().size() - 1)
		                                ? selectedIdx - 1
		                                : selectedIdx;

		                authorList.getItems().remove(selectedIdx);
		                authorList.getSelectionModel().select(newSelectedIdx);
		                //removes the author for the array
		                System.out.println("selectIdx: " + selectedIdx);
		                System.out.println("item: " + itemToRemove);
		                authors.remove(selectedIdx);

		            }
		        }
		    });
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
		
		
		if(ISBN.length() != bookController.getISBNMaxLength()) {
			emptyFieldsLbl.setText("ISBN must be at 13 digits");
			return null;
		}
		
		if(ISBN.isEmpty() || title.isEmpty()|| publisher.isEmpty()
				|| category == null || _noCopies.isEmpty() || _threshold.isEmpty()
				|| _price.isEmpty()|| year == null) {
			emptyFieldsLbl.setText("Some fields are empty or not selected");
			return null;
		}
		
		Integer noCopies =  Integer.parseInt(noCopiesTxt.getText().trim());
		Integer threshold = Integer.parseInt(_threshold);
		Integer sellingPrice= Integer.parseInt(_price);
		
		newBook = new Book(ISBN,title,category, sellingPrice,noCopies,threshold,publisher, 
				year+"-1-1" );
		
		return newBook;
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		bookController = new BookController();
		if(bookController.isConnected()) {
			authors = new ArrayList<>();
			
			//set validation input to text field
			UtilControl.addValidation(noCopiesTxt, DIGRGX, NDIGRGX);
			UtilControl.addValidation(thresholdTxt, DIGRGX, NDIGRGX);
			UtilControl.addValidation(isbnTxt, DIGRGX, NDIGRGX);
			UtilControl.addValidation(sellingPriceTxt, DIGRGX, NDIGRGX);
			UtilControl.addValidation(authorTxt, WORDRGX, NWORDRGX);
			
			//set limitation length of each text field
			UtilControl.addTextLimiter(isbnTxt, bookController.getISBNMaxLength());
			UtilControl.addTextLimiter(titleTxt, bookController.getTitleMaxLenth());
			UtilControl.addTextLimiter(publishertxt, bookController.getPublisherMaxLength());
			UtilControl.addTextLimiter(authorTxt, bookController.getAuthorMaxLength());
			UtilControl.addTextLimiter(noCopiesTxt, MAXINTLENGHT);
			UtilControl.addTextLimiter(thresholdTxt, MAXINTLENGHT);
			UtilControl.addTextLimiter(sellingPriceTxt, MAXINTLENGHT);
			
			//initiate combo boxes
			initCatigories();
			initYears();
		} else {
			errorLabel.setText("Server is disconnected");
		}
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
		
		errorLabel.setText("");;
		emptyFieldsLbl.setText("");
	}
	
	/*
	 * Add categories to Category combo box.
	 */
	private void initCatigories() {
		ObservableList<String> categories = FXCollections.observableArrayList(bookController.getCategories());
		categoryComBx.setItems(categories);
	}
	
	/*
	 * Add years to year combo box.
	 */
	private void initYears() {
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		Integer[] years = new Integer[YEARS];
		for(int i = 0; i < YEARS; i++)years[i] = curYear - i;
		ObservableList<Integer> pYears = FXCollections.observableArrayList(years);
		yearComBx.setItems(pYears);
	}
}
