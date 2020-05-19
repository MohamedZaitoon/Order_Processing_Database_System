package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.Calendar;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.BookController;
import utils.User;

public class UtilControl {

	/*
	 * No. years Available for publication year.
	 */
	private final static int YEARS = 100;
	/*
	 * Max length for integer textfield
	 */
	public final static int MAXINTLENGHT = 9;
	/*
	 * Regex that matches only digits.
	 */
	public static final String DIGRGX = "\\d*";
	public static final String NDIGRGX = "[^\\d]*";
	/*
	 * Regex that matches only word characters.
	 */
	public static final String WORDRGX = "[\\s\\w]*";
	public static final String NWORDRGX = "[^\\s\\w]";
	
	
	private static BookController bookController = new BookController();
	/*
	 * Set max length of input to Textfield
	 */
	public static void addTextLimiter(final TextField tf, final int maxLength) {
		tf.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> ov, final String oldValue,
					final String newValue) {
				if (tf.getText().length() > maxLength) {
					String s = tf.getText().substring(0, maxLength);
					tf.setText(s);
				}
			}
		});
	}

	/*
	 * Enter only numbers
	 */
	public static void addValidation(final TextField tf, String rgx, String nRgx) {
		tf.textProperty().addListener((args, oldValue, newValue) -> {

			if (!newValue.matches(rgx)) {
				tf.setText(newValue.replaceAll(nRgx, ""));
			}
		});
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void buildTable(TableView tableView, ObservableList<ObservableList> data, ResultSet rs) {

		data = FXCollections.observableArrayList();
		try {
			/**
			 * ******************************** 
			 * TABLE COLUMN ADDED DYNAMICALLY *
			 *********************************
			 */
			for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
				// We are using non property style for making dynamic table
				final int j = i;
				TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
				col.setCellValueFactory(
						new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
							public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
								return new SimpleStringProperty(param.getValue().get(j).toString());
							}
						});

				tableView.getColumns().addAll(col);
//				System.out.println("Column [" + i + "] ");
			}
			
			/**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
//                System.out.println("Row [1] added " + row);
                data.add(row);
 
            }
 
            //FINALLY ADDED TO TableView
            tableView.setItems(data);
            
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	@SuppressWarnings("rawtypes")
	public static void disableReorderColumns(TableView tableView) {
		tableView.widthProperty().addListener(new ChangeListener<Number>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
		    {
		        TableHeaderRow header = (TableHeaderRow) tableView.lookup("TableHeaderRow");
		        header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
		            @Override
		            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		                header.setReordering(false);
		            }
		        });
		    }
		});
	}  
	
	/*
	 * Add categories to Category combo box.
	 */
	public static void initCatigories(ComboBox<String> comBx) {
		ObservableList<String> categories = FXCollections.observableArrayList(bookController.getCategories());
		comBx.setItems(categories);
	}
	
	/*
	 * Add years to year combo box.
	 */
	public static void initYears(ComboBox<Integer> comBx) {
		int curYear = Calendar.getInstance().get(Calendar.YEAR);
		Integer[] years = new Integer[YEARS];
		for(int i = 0; i < YEARS; i++)years[i] = curYear - i;
		ObservableList<Integer> pYears = FXCollections.observableArrayList(years);
		comBx.setItems(pYears);
	}
	
	
	
	public static final void changeScene(Event event, URL url, User user) {
    	try {
            Node node = (Node) event.getSource();
            Stage stage = (Stage)  node.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            if(user != null && fxmlLoader.getController() instanceof DelegateUser) {
            	((DelegateUser)fxmlLoader.getController()).setUser(user);
            }
            stage.close();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
	
	
	
}
