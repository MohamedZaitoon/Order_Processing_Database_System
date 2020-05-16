package model;

public class OperationResponse {
	
	private boolean executedSuccessfuly;
	
	private String errorMessage;
	
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
}
