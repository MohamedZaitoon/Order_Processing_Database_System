package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import model.OperationResponse;
import model.OrderController;

public class OrdersViewController implements Initializable {
	@FXML
	private TableView ordersTable;
	@FXML
	private Button confirmBtn;

	@FXML
	private Button refreshBtn;

	@FXML
	private Label successLbl;

	@FXML
	private Label errorLbl;

	private OrderController orderCont;

	ObservableList<ObservableList> data;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		orderCont = new OrderController();
		ordersTable.setPlaceholder(new Label("No rows to display"));
		if (orderCont.isConnected()) {
			OperationResponse or = orderCont.getOrders();
			if (or.isExecutedSuccessfuly()) {
				buildTable(or.getResultSet());
			} else {
				errorLbl.setText(or.getErrorMessage());
			}

		} else {
			disconnectedServerError();
		}
	}

	@FXML
	void confirmListener(ActionEvent event) {
		TableViewSelectionModel sm = ordersTable.getSelectionModel();
		ObservableList row = sm.getSelectedItems();
	
		clearLabels();
		if (row.get(0) != null) {
			if(orderCont.isConnected()) {
				final int selectedIndex = sm.getSelectedIndex();
				String idOrder = row.get(0).toString().split(",")[0].replaceAll("\\[", "");
				System.out.println(idOrder);
				orderCont.confirmOrder(Integer.parseInt(idOrder));
				ordersTable.getItems().remove(selectedIndex);
				successLbl.setText("Order "+idOrder+ " Confirmed");
//				sm.clearSelection();
			}else {
				disconnectedServerError();
			}
		}
	}

	@FXML
	void refreshOrdersListener(ActionEvent event) {
		clearLabels();
			if(orderCont.isConnected()) {
				OperationResponse or = orderCont.getOrders();
				if(or.isExecutedSuccessfuly()) {
					ordersTable = new TableView();
					this.buildTable(or.getResultSet());
				} else {
					errorLbl.setText(or.getErrorMessage());
				}
				
			}else {
				disconnectedServerError();
			}
		
	}

	
	private void clearLabels() {
		successLbl.setText("");
		errorLbl.setText("");
	}
	
	private void disconnectedServerError() {
		errorLbl.setText("Server is disconnected");
	}
	
	private void buildTable(ResultSet rs) {
		UtilControl.buildTable(ordersTable, data, rs);
		UtilControl.autoFitTable(ordersTable);
		UtilControl.disableReorderColumns(ordersTable);
	}
}
