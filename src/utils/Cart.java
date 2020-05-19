package utils;

import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.regex.*;

public class Cart {
	
	
	private Map<String, Book> cart;
	
	private int totalCost;
	
	private final String CREDIT_VALIDATION = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7]"
			+ "[0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47]"
			+ "[0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
	private static Pattern p;
	
	public Cart() {
		totalCost = 0;
		cart = new HashMap<String, Book>();
		p = Pattern.compile(CREDIT_VALIDATION);
	}
	
	public void addBook(Book book) {
		String ISBN = book.getISBN();
		totalCost += Integer.parseInt(book.getSellingPrice());
		book.setNoCopies(Integer.toString(Integer.parseInt(book.getNoCopies()) - 1));
		book.setCartCopies(Integer.toString(Integer.parseInt(book.getCartCopies()) + 1));
		cart.put(ISBN, book);
	}
	
	public void removeBook(Book book) {
		String ISBN = book.getISBN();
		totalCost -= Integer.parseInt(book.getSellingPrice());
		book.setNoCopies(Integer.toString(Integer.parseInt(book.getNoCopies()) + 1));
		if(Integer.parseInt(book.getCartCopies()) == 1) {
			cart.remove(ISBN);
		} else {
			book.setCartCopies(Integer.toString(Integer.parseInt(book.getCartCopies()) - 1));
			cart.put(ISBN, book);
		}
	}
	
	public void emptyCart() {
		totalCost = 0;
		cart.clear();
	}
	
	public static boolean isValidCredit(String credit) {
		Matcher m = p.matcher(credit);
		return m.matches();
	}
	
	public int getCartSize() {
		return cart.size();
	}
	
	public ObservableList<Book> getCartBooks() {
		ObservableList<Book> data = FXCollections.observableArrayList();
		for (Book book : cart.values()) {
			data.add(book);
		}
		return data;
	}
	
	public int getTotalCost() {
		return totalCost;
	}
	
	public int getCartCopies(Book book) {
		return Integer.parseInt(book.getCartCopies());
	}
	
	public int getAvailableCopies(Book book) {
		return Integer.parseInt(book.getNoCopies());
	}

}
