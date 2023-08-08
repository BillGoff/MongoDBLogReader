package snaplogic.mongodb.monitor.utils;

import java.util.Collection;

import snaplogic.mongodb.monitor.dto.LogEntry;

public class FindUtil {

	public static String getQuery(String queryHash, Collection<LogEntry> entries)
	{	
		for(LogEntry entry: entries)
		{
			if(queryHash.equalsIgnoreCase(entry.getQueryHash()))
			{
				return entry.getCmd();
			}
		}
		return "";
	}
}
