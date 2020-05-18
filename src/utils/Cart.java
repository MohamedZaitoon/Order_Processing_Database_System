package utils;

import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Cart {
	
	
	private Map<String, Book> cart;
	
	private int totalCost;
	
	public Cart() {
		totalCost = 0;
		cart = new HashMap<String, Book>();
	}
	
	public void addBook(Book book) {
		String ISBN = book.getISBN();
		totalCost += Integer.parseInt(book.getSellingPrice());
		book.setNoCopies(Integer.toString(Integer.parseInt(book.getNoCopies()) - 1));
		book.setCartCopies(Integer.toString(Integer.parseInt(book.getCartCopies()) + 1));
		cart.put(ISBN, book);
	}
	
	public void remove(Book book) {
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
	
	public ObservableList<Book>  getCartBooks() {
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
