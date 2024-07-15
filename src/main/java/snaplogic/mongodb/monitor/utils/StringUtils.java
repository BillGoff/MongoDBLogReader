package snaplogic.mongodb.monitor.utils;

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

}
