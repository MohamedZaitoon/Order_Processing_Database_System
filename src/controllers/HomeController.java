package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.sun.org.apache.xpath.internal.operations.Mod;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import utils.*;

public class HomeController implements Initializable {
	
	@FXML
    private JFXButton btnAccount;

    @FXML
    private JFXButton btnLogOut;

    @FXML
    private JFXTextField txtSearch;

    @FXML
    private JFXButton btnAddToCart;

    @FXML
    private TableView<Book> booksTable;

    @FXML
    private TableColumn<Book, String> booksTableISBN;

    @FXML
    private TableColumn<Book, String> booksTableTitle;

    @FXML
    private TableColumn<Book, String> booksTableAuthor;

    @FXML
    private TableColumn<Book, String> booksTableCategory;

    @FXML
    private TableColumn<Book, String> booksTableCopies;

    @FXML
    private TableColumn<Book, String> booksTablePublisher;

    @FXML
    private TableColumn<Book, String> booksTablePYear;

    @FXML
    private TableColumn<Book, String> booksTablePrice;

    @FXML
    private TableView<Book> cartTable;

    @FXML
    private TableColumn<Book, String> cartTableISBN;

    @FXML
    private TableColumn<Book, String> cartTableTitle;
    
    @FXML
    private TableColumn<Book, String>  cartTableCopies;

    @FXML
    private TableColumn<Book, String> cartTablePrice;

    @FXML
    private JFXComboBox<String> comboCategory;
    
    @FXML
    private JFXComboBox<String> comboSearchBy;

    @FXML
    private JFXButton btnRemCart;

    @FXML
    private JFXTextField txtCreditNumber;

    @FXML
    private JFXButton btnSearch;

    @FXML
    private JFXButton btnManage;

    @FXML
    private JFXDatePicker dbkExpirationData;

    @FXML
    private JFXTextField txtCVV;

    @FXML
    private JFXButton btnPay;

    @FXML
    private Label lblCreditNumber;

    @FXML
    private Label lblExpirationDate;

    @FXML
    private Label lblCreditPay;

    @FXML
    private Label lblBooksTable;
    
    @FXML
    private Label lblServerConnection;
    
    @FXML
    private JFXButton btnEdit;
	
	private User user;
	private Cart cart;
	
	private final String ALL_CATEGORIES = "All";
	private final String DEFAULT = "Default";
	
	private Connection connection = null;
    private PreparedStatement preparedStatement = null;
	
	@FXML
    public void handleButtonTransition(MouseEvent event) {
        if (event.getSource() == btnLogOut) {
        	changeScene(event, StatusUtil.LOGIN_URL);
        } else if (event.getSource() == btnManage) {
        	changeScene(event, "/fxml/ManagerPage.fxml");
        } else if (event.getSource() == btnAccount) {
        	
        }else if (event.getSource() == btnEdit) {
        	System.out.println("Edit button");
        	changeScene(event, "/fxml/ModifyBook.fxml");
        }
    }
	
	@FXML void handleButtonAction(MouseEvent event) {
		if (event.getSource() == btnSearch) {
			booksTable.getItems().clear();
			String key = txtSearch.getText();
			if (key.length() == 0 && comboSearchBy.getValue() == DEFAULT) {
				fillBooksTable(comboCategory.getValue());
			}
			
		} else if (event.getSource() == btnAddToCart) {
			Book book = booksTable.getSelectionModel().getSelectedItem();
			if (book == null) {
				StatusUtil.setLblError(lblBooksTable, Color.TOMATO, "Please, Select a book to add.");
			} else {
				if (cart.getAvailableCopies(book) > 0) {
					cart.addBook(book);
					cartTable.setItems(cart.getCartBooks());
					cartTable.refresh();
					booksTable.refresh();
					StatusUtil.setLblError(lblBooksTable, Color.GREEN, "Book added to cart.");
				} else {
					StatusUtil.setLblError(lblBooksTable, Color.TOMATO, "No More Copies");
				}
			}
		}
	}
	
	
	private void changeScene(MouseEvent event, String path) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            
            // ------------- 
            if (event.getSource() == btnManage) {
            	
            } else if (event.getSource() == btnAccount) {
            	
            }else if (event.getSource() == btnEdit) {
            	System.out.println("COntroller");
            	String isbn = booksTable.getSelectionModel().getSelectedItem().getISBN();
            	ModifyBookViewController controller = fxmlLoader.<ModifyBookViewController>getController();
            	controller.setParent(StatusUtil.HOME_URL);
            	controller.setISBN(isbn);
            	System.out.println("Loader");
            }
            // -------------
            
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		StatusUtil.notifyConnectionStatus(connection, lblServerConnection);
    	initializeBooksTableColumns();
    	initializeCartTableColumns();
		comboCategory.getItems().addAll(ALL_CATEGORIES, "Art", "Geography", "History", "Religion" ,"Science");
		comboSearchBy.getItems().addAll(DEFAULT, "Title", "Author", "Publisher");
		comboCategory.getSelectionModel().select(0);
		comboSearchBy.getSelectionModel().select(0);
		fillBooksTable(comboCategory.getValue());
	}
	
	private ResultSet filterBy(String category) {
		try {
					
			String sql;
			if (category == ALL_CATEGORIES) {
				sql = "SELECT B.ISBN, B.title, GROUP_CONCAT(A.author_name), B.category, B.no_copies, B.publisher, B.pyear, B.selling_price "
						+ "FROM book AS B "
						+ "LEFT JOIN authors AS A "
						+ "ON B.ISBN = A.ISBN "
						+ "GROUP by ISBN";
				preparedStatement = connection.prepareStatement(sql);
			} else  {
				sql = "SELECT B.ISBN, B.title, GROUP_CONCAT(A.author_name), B.category, B.no_copies, B.publisher, B.pyear, B.selling_price "
						+ "FROM book AS B "
						+ "LEFT JOIN authors AS A "
						+ "ON B.ISBN = A.ISBN "
						+ "WHERE Category = ? "
						+ "GROUP by ISBN";
				preparedStatement = connection.prepareStatement(sql);
	        	preparedStatement.setString(1, comboCategory.getValue());
			}
			return preparedStatement.executeQuery();
		} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            return null;
        }
	}
	
	private void fillBooksTable(String category) {
		try {
			ObservableList<Book> data = FXCollections.observableArrayList();
			ResultSet resultSet = filterBy(category);
			if (resultSet != null) {
				
	        	while (resultSet.next()) {
	        		data.add(new Book(resultSet.getString(1), resultSet.getString(2),
	        				resultSet.getString(3), resultSet.getString(4),
	        				resultSet.getString(5), resultSet.getString(6),
	        				resultSet.getString(7), resultSet.getString(8)));
	        	}
			}
        	booksTable.setItems(data);
    	} catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
        }
	}
	
	private void initializeBooksTableColumns() {
    	booksTableISBN.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
    	booksTableTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
    	booksTableAuthor.setCellValueFactory(new PropertyValueFactory<Book, String>("authorName"));
    	booksTableCategory.setCellValueFactory(new PropertyValueFactory<Book, String>("category"));
    	booksTablePrice.setCellValueFactory(new PropertyValueFactory<Book, String>("sellingPrice"));
    	booksTableCopies.setCellValueFactory(new PropertyValueFactory<Book, String>("noCopies"));
    	booksTablePublisher.setCellValueFactory(new PropertyValueFactory<Book, String>("publisher"));
    	booksTablePYear.setCellValueFactory(new PropertyValueFactory<Book, String>("pYear"));
	}
	
	private void initializeCartTableColumns() {
		cartTableISBN.setCellValueFactory(new PropertyValueFactory<Book, String>("ISBN"));
		cartTableTitle.setCellValueFactory(new PropertyValueFactory<Book, String>("title"));
		cartTableCopies.setCellValueFactory(new PropertyValueFactory<Book, String>("cartCopies"));
    	cartTablePrice.setCellValueFactory(new PropertyValueFactory<Book, String>("sellingPrice"));
	}
	
	public HomeController() {
		connection = ConnectionUtil.connectDatabase();
		cart = new Cart();
	}
	
	public void setUser(User user) {
		if (user != null) {
			this.user = user;
			if (this.user.getUserRole() == User.MANAGER) {
				btnManage.setDisable(true);
				btnManage.setVisible(true);
			} else {
				btnManage.setDisable(false);
				btnManage.setVisible(false);
			}
		}
	}
}
