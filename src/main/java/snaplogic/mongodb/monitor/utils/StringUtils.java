package snaplogic.mongodb.monitor.utils;

import static org.apache.commons.lang3.StringEscapeUtils.escapeHtml4;

/**
 * (U) This class is any and all String Utils used by the MongoDBLogReader.
 * @author bgoff
 * @since 13 Nov 2023
 */
public class StringUtils {
	
	public static String escapeDoubleQuote(String myString)
	{
		if(myString != null)
			return myString.replaceAll("\"", "\\\\\"");
		else
			return "";
	}
	
	public static String escapeNewLines(String myString)
	{
		if(myString != null)
			return (myString.replace("\\n", " "));
		else
			return "";
	}
	
	@SuppressWarnings("deprecation")
	public static String escapeSpecialChars(String myString)
	{
		if(myString != null)
		{
			myString = escapeHtml4(myString);
			return (myString.replaceAll("[\\t\\n\\r]+"," "));
		}
		else
			return "";
	}

}
