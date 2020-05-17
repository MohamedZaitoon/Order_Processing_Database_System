package controllers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

public class UtilControl {

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
}
