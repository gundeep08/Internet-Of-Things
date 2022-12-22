package cscie97.store.model;

public class CommandProcessorException extends Exception {
	
	private String command;
	private String reason;
	private int lineNumber;
	
	public CommandProcessorException(String command, String reason, int lineNumber) {
		super("CommandProcessor Exception while executing " + command + " because "+ reason + "at line: " +lineNumber); 
		this.command = command;
		this.reason = reason;
		this.lineNumber = lineNumber;
	}
	public String getCommand() {
		return command;
	}
	public void setCommand(String command) {
		this.command = command;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	
}
