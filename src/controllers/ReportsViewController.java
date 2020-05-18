package controllers;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import model.OperationResponse;
import model.ReportController;
import model.ReportController.OperationType;

public class ReportsViewController implements Initializable {
	
	@SuppressWarnings("rawtypes")
	@FXML
	private TableView tableView;
	@FXML
	private Button top10BooksBtn;
	@FXML
	private Button top5UserBtn;
	@FXML
	private Button totalSalesBtn;
	@FXML
	private Label totalSales;

	@FXML
	private Label errorLabel;

	private ReportController reportController;

	@SuppressWarnings("rawtypes")
	private ObservableList<ObservableList> data;

	// Event Listener on Button[#top10BooksBtn].onAction
	@FXML
	public void showTop10BooksListener(ActionEvent event) {
		buildTable(OperationType.TopTenBooks);
		System.out.println("Top 10");
	}

	// Event Listener on Button[#top5UserBtn].onAction
	@FXML
	public void showTop5UserListener(ActionEvent event) {
		buildTable(OperationType.TopFiveUser);
		System.out.println("Top five");
	}

	// Event Listener on Button[#totalSalesBtn].onAction
	@FXML
	public void showTotalSalesListener(ActionEvent event) {
		errorLabel.setText("");
		if (reportController.isConnected()) {
			OperationResponse or = reportController.getDataOfOperation(OperationType.TotalSales);
			if (or.isExecutedSuccessfuly()) {
				ResultSet rs = or.getResultSet();
				try {
					if (rs.next()) {
						Integer totalNoCopies = rs.getInt(1);
						Integer totalCost = rs.getInt(2);
						String str = "Total No. Copies sold: \n" + totalNoCopies.toString() + "\n" + "Total Sales: \n"
								+ totalCost.toString();
						totalSales.setText(str);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}

			} else {
				errorLabel.setText(or.getErrorMessage());
			}

		} else {
			disconnectedServerError();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		reportController = new ReportController();
		if (reportController.isConnected()) {
			clearControllers();
		} else {
			disconnectedServerError();
		}
	}

	private void buildTable(OperationType opt) {
		errorLabel.setText("");
		if (reportController.isConnected()) {
			OperationResponse or = reportController.getDataOfOperation(opt);
			System.out.println(or.isExecutedSuccessfuly());
			if (or.isExecutedSuccessfuly()) {
				tableView.getItems().clear(); 
				tableView.getColumns().clear();
				UtilControl.buildTable(tableView, data, or.getResultSet());
				UtilControl.disableReorderColumns(tableView);
				
				// Delete Redundant column from tabel 'ISBN'
				if(opt == OperationType.TopTenBooks) {
					tableView.getColumns().remove(0);
				}
			} else {
				errorLabel.setText(or.getErrorMessage());
			}

		} else {
			disconnectedServerError();
		}

	}

	void clearControllers() {
		totalSales.setText("");
		errorLabel.setText("");
	}

	private void disconnectedServerError() {
		errorLabel.setText("Server is disconnected");
	}
}
