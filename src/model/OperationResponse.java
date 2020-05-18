package model;

import java.sql.ResultSet;

public class OperationResponse {

	private boolean executedSuccessfuly;

	private String errorMessage;

	private ResultSet resultSet;


	public ResultSet getResultSet() {
		return resultSet;
	}

	public OperationResponse() {
	}

	public boolean isExecutedSuccessfuly() {
		return executedSuccessfuly;
	}

	public void setExecutedSuccessfuly(boolean executedSuccessfuly) {
		this.executedSuccessfuly = executedSuccessfuly;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
	}
}
