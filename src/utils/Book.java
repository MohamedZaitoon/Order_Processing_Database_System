package utils;

import javafx.beans.property.SimpleStringProperty;

public class Book {
	private SimpleStringProperty ISBN;
	private SimpleStringProperty title;
	private SimpleStringProperty authorName;
	private SimpleStringProperty category;
	private SimpleStringProperty sellingPrice;
	private SimpleStringProperty noCopies;
	private SimpleStringProperty cartCopies;
	private SimpleStringProperty threshold;
	private SimpleStringProperty publisher;
	private SimpleStringProperty pYear;
	
	private final String THRESHOLD = "0"; 
	
	public Book(String ISBN, String title, String authorName, String category, String noCopies
			, String publisher, String pYear, String sellingPrice) {
		this.threshold = new SimpleStringProperty(THRESHOLD);
		this.ISBN = new SimpleStringProperty(ISBN);
		this.title = new SimpleStringProperty(title);
		this.authorName = new SimpleStringProperty(authorName);
		this.category = new SimpleStringProperty(category);
		this.sellingPrice = new SimpleStringProperty(sellingPrice);
		this.noCopies = new SimpleStringProperty(noCopies);
		this.publisher = new SimpleStringProperty(publisher);
		this.pYear = new SimpleStringProperty(pYear);
		this.cartCopies = new SimpleStringProperty("0");
	}
	
	public String getISBN() {
		return ISBN.get();
	}
	
	public void setISBN(String ISBN) {
		this.ISBN.set(ISBN);
	}
	
	public String getTitle() {
		return title.get();
	}
	
	public void setTitle(String title) {
		this.title.set(title);
	}
	
	public String getAuthorName() {
		return authorName.get();
	}
	
	public void setAuthoName(String authorName) {
		this.authorName.set(authorName);
	}
	
	public String getCategory() {
		return category.get();
	}
	
	public void setCategory(String category) {
		this.category.set(category);
	}
	
	public String getSellingPrice() {
		return sellingPrice.get();
	}
	
	public void setSellingPrice(String sellingPrice) {
		this.sellingPrice.set(sellingPrice);
	}
	
	public String getNoCopies() {
		return noCopies.get();
	}
	
	public void setNoCopies(String noCopies) {
		this.noCopies.set(noCopies);
	}
	
	public String getCartCopies() {
		return cartCopies.get();
	}
	
	public void setCartCopies(String cartCopies) {
		this.cartCopies.set(cartCopies);
	}
	
	public String getThreshold() {
		return threshold.get();
	}
	
	public void setThreshold(String threshold) {
		this.threshold.set(threshold);
	}
	
	public String getPublisher() {
		return publisher.get();
	}
	
	public void setPublisher(String publisher) {
		this.publisher.set(publisher);
	}
	
	public String getPYear() {
		return pYear.get();
	}
	
	public void setPYear(String pYear) {
		this.pYear.set(pYear);
	}
	
}
