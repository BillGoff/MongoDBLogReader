package snaplogic.mongodb.monitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This class is the Data Transfer Object (DTO) used to hold the String pulled from the Log Entry date field.
 * @author bgoff
 *
 */
public class LogEntryDate {

	@JsonProperty("$date")
	private String dateString;
	

	public String getDateString() {
		return dateString;
	}
	
	
	public void setDateString(String dateString) {
		this.dateString = dateString;
	}
	
	/**
	 * (U) Method used to printout the values of this Class.
	 *
	 * @return String nice readable string value of this class.
	 */
	@Override
	public String toString()
	{
		return (toString(""));
	}
	
	/**
	 * (U) Convenience method to make the string printed out indent as intended.
	 *
	 * @param tabs String value for the tabs used for indentation.
	 * @return String nice readable string value of this class.
	 */
	public String toString(String tabs)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(tabs + "date: " + this.getDateString() + "\n");
		
		return sb.toString();
	}
}
