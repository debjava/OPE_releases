package com.ope.patu.exception;

public class FileRetrievalException extends Exception {

	private String errCode;

	public FileRetrievalException() {
		super();
	}

	public FileRetrievalException(String msg) {
		super(msg);
	}

	/**
	 * Create a new business exception
	 * 
	 * @param errorCode
	 * 
	 * @param message
	 *            Human-readable message (will appear in logs)
	 */
	public FileRetrievalException(String errorCode, String message) {
		super(message);
		this.errCode = errorCode;
	}

	/**
	 * Wrap a lower-level exception inside a FileRetrievalException
	 * 
	 * @param errorCode
	 *            Detailed error code
	 * @param message
	 *            Human-readable message (will appear in logs)
	 * @param cause
	 *            Root cause of this exception
	 */
	public FileRetrievalException(String errorCode, String message,
			Exception cause) {
		super(message, cause);
		this.errCode = errorCode;
	}

	/**
	 * 
	 * @return
	 */
	public String getErrorCode() {
		return errCode;
	}
}
