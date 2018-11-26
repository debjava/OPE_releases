package com.ope.scheduler.exception;

/**
 * Parent class for all Runtime exceptions that can be generated within our
 * applications
 */
public class OPERuntimeException extends RuntimeException {
	/** Reference to error code */
	private String errCode;

	/**
	 * Create a new runtime exception
	 * 
	 * @param errorCode
	 *            Detailed error code (should be found in IErrorCodes and in and
	 *            localized error messages file)
	 * @param message
	 *            Human-readable message (will appear in logs)
	 */
	public OPERuntimeException(String errorCode, String message) {
		super(message);
		this.errCode = errorCode;
	}

	/**
	 * Wrap a lower-level exception in a MdcRuntimeException
	 * 
	 * @param errorCode
	 *            Detailed error code (should be found in IErrorCodes and in
	 *            localized errormessages file)
	 * @param message
	 *            Human-readable message (will appear in logs)
	 * @param cause
	 *            Root cause of this exception
	 */
	public OPERuntimeException(String errorCode, String message, Exception cause) {
		super(message, cause);
		this.errCode = errorCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see mdc.exception.ErrorCodeException#getErrorCode()
	 */
	public String getErrorCode() {
		return errCode;
	}
}
