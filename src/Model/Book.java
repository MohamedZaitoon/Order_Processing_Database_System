package Model;


/*
 * Representation of  a book
 */
public class Book {

	private String ISBN;
	private String title;
	private String category;
	private Integer sellingPrice;
	private Integer noCopies;
	private Integer threshold;
	public Integer getThreshold() {
		return threshold;
	}

	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	private String Publisher;
	private String publicationYear;
	private String authors;

	public Book(String _ISBN, String _title, String _category, Integer _sellingPrice, 
			Integer _noCopies, Integer _threshold, String _publisher,String _publicationYear) {
	   setISBN(_ISBN); 
	   setTitle(_title);
	   setCategory(_category);
	   setSellingPrice(_sellingPrice);
	   setNoCopies(_noCopies);
	   setThreshold(_threshold);
	   setPublisher(_publisher);
	   setPublicationYear(_publicationYear);
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Integer getSellingPrice() {
		return sellingPrice;
	}

	public void setSellingPrice(Integer sellingPrice) {
		this.sellingPrice = sellingPrice;
	}

	public Integer getNoCopies() {
		return noCopies;
	}

	public void setNoCopies(Integer noCopies) {
		this.noCopies = noCopies;
	}

	public String getPublisher() {
		return Publisher;
	}

	public void setPublisher(String publisher) {
		Publisher = publisher;
	}

	public String getPublicationYear() {
		return publicationYear;
	}

	public void setPublicationYear(String publicationYear) {
		this.publicationYear = publicationYear;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

}
