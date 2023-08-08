package snaplogic.mongodb.monitor.exceptions;

/**
 * This exception class is the upper level exception class for the
 * MongoDBLogReader application.
 * 
 * @author bgoff
 * @since 3 Aug 2023
 */
public class MongoDbLogReaderException extends Exception {

	private static final long serialVersionUID = 6460453393868100518L;

	/**
	 * (U) Constructs a new MongoDbLogReaderException with null as its details.
	 */
	public MongoDbLogReaderException() {
	}

	/**
	 * (U) Constructs a new MongoDbLogReaderException with the specified detail
	 * message.
	 *
	 * @param message String value to set the message to.
	 */
	public MongoDbLogReaderException(String message) {
		super(message);
	}

	/**
	 * (U) Constructs a new MongoDbLogReaderException with the specified detail
	 * message and cause.
	 *
	 * @param message String value to set the message to.
	 * @param cause   Throwable class to set the cause to.
	 */
	public MongoDbLogReaderException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * (U) Constructs a new MongoDbLogReaderException wit the specified detail
	 * message, cause, suppression flag set to either enabled or disabled, and the
	 * writable stack trace flag set to either enable or disabled.
	 *
	 * @param message             String value to set the message to.
	 * @param cause               Throwable class to set the cause to.
	 * @param enableSuppression   boolean used to set the enabled suppression flag
	 *                            to.
	 * @param writeableStackTrace boolean used to set the write able stack trace
	 *                            flag to.
	 */
	public MongoDbLogReaderException(String message, Throwable cause, boolean enableSuppression,
			boolean writeableStackTrace) {
		super(message, cause, enableSuppression, writeableStackTrace);
	}

	/**
	 * (U) Constructs a new MongoDbLogReaderException with the cause set.
	 * 
	 * @param cause Throwable class to set the cause to.
	 */
	public MongoDbLogReaderException(Throwable cause) {
		super(cause);
	}
}
