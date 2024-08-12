package snaplogic.mongodb.monitor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import snaplogic.mongodb.monitor.dto.LogEntry;
import snaplogic.mongodb.monitor.dto.LogEntryDate;
import snaplogic.mongodb.monitor.dto.QueryMetadata;
import snaplogic.mongodb.monitor.exceptions.MongoDbLogReaderException;

/**
 * This class is used to read the log file. It produces a Map with the unique
 * query hash as a key and a QueryMetadata object that contains details of the
 * entry.
 * 
 * @author bgoff
 * @since 7 August 2023
 */
public class LogFileReader {
	private static final Logger logger = LogManager.getLogger("MongoDBLogReader");

	private static Date endLogDate = null;	
	private static Date startLogDate = null;

	/**
	 * (U) This method is used to get newest date of the logs that where processed.
	 * @return Date the newest date from the log file.
	 */
	public static Date getEndDate()
	{	
		return endLogDate;
	}
	
	/**
	 * (U) This private static method is used to get a handle to the file supplied via the Command Line Interface 
	 * options.
	 * @param cli Command Line Interface 
	 * @param option String value of the option that identifies the file we are attempting to read from.
	 * @return File a handle to the file we are attempting to read from.
	 * @throws MongoDbLogReaderException if we are unable to read from the file.
	 */
	private static File getFile(CommandLine cli, String option) throws MongoDbLogReaderException {
		File file = null;
		
		if (cli.hasOption(option)) 
		{
			String fileName = cli.getOptionValue(option);

			if (logger.isDebugEnabled())
				logger.debug("Attempting to read log file (" + fileName + ")");

			if ((fileName != null) && (fileName.trim().length() > 0)) 
			{
				file = new File(fileName);
				if (!file.exists()) 
					throw new MongoDbLogReaderException("File(" + fileName + ") does NOT exist!");
				if (!file.canRead())							
					throw new MongoDbLogReaderException("Unable to read MongoDB log file.  Check your permissions " +
							"for file(" + fileName + ").");
			}
			else
				throw new MongoDbLogReaderException("No file name priveded.");
		} else
			throw new MongoDbLogReaderException("You must provide a file for us to read from!");
		return file;
	}
	
	/**
	 * (U) This method is used to get the start date entry for the log file that was processed.
	 * @return Date the start date of the log file.
	 */
	public static Date getStartDate()
	{
		return startLogDate;
	}
	
	/**
	 * (U) This method is used to get the Log Entry Date from the Log Entry.
	 * @param line String the log entry.
	 * @return LogEntryDate pulled from the log Entry.
	 * @throws JsonMappingException
	 * @throws JsonProcessingException
	 */
	public static LogEntryDate parseLogDateFromString(String line) 
			throws JsonMappingException, JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();
			
		LogEntry logEntry = null;

		logEntry = mapper.readValue(line, LogEntry.class);

		return logEntry.getLogEntryDate();
	}

	/**
	 * This method is used to calculate out the start and end dates of the log entries.
	 * @param logEntry
	 */
	private static void parseLogDates(LogEntry logEntry)
	{
		Date logEntryDate = DateUtils.toDate(logEntry.getLogEntryDate().getDateString());
		
		if(startLogDate == null)
			startLogDate = logEntryDate;
		else if(startLogDate.after(logEntryDate))
			startLogDate = logEntryDate;
		
		if(endLogDate == null)
			endLogDate = logEntryDate;
		else if(endLogDate.before(logEntryDate))
			endLogDate = logEntryDate;
		
	}

	/**
	 * This public method is used to call the code that will produce the Map of the
	 * query hash to its query metadata.
	 * 
	 * @param cli    CommandLine option that tells us are parsing a file.
	 * @param option String value that tells us the name of the file we are parsing.
	 * @return Map containing the query hash as a string and the query metadata
	 *         object the contains the details of the query.
	 * @throws MongoDbLogReaderException if we are unable to parse the file into the
	 *                                   map.
	 */
	public static Map<String, QueryMetadata> parseLogFile(CommandLine cli, String option)
			throws MongoDbLogReaderException {
		Map<String, QueryMetadata> uniqueQueries = null;
		
		File file = getFile(cli, option);
				
		uniqueQueries = parseLogFile(file);

		
		return uniqueQueries;
	}
	
	/**
	 * (U) This method is used to do a deep dive parse into all the log entries within the log file.
	 * @param cli    CommandLine option that tells us are parsing a file.
	 * @param option String value that tells us the name of the file we are parsing.	
	 * @return List of all the LogEntries (queries/inserts/updates) pulled from the log file.
	 * @throws MongoDbLogReaderException if we run into an issue parsing the log file.
	 */
	public static List<LogEntry> deepDiveParseLogFile(CommandLine cli, String option)
			throws MongoDbLogReaderException {
		
		List<LogEntry> logEntries = new ArrayList<LogEntry>();
		
		File logFile = getFile(cli, option);
		
		try {
			LogEntry logEntry = null;
			ObjectMapper mapper = new ObjectMapper();

			try (BufferedReader br = new BufferedReader(new FileReader(logFile))) 
			{
				String line;
				while ((line = br.readLine()) != null) 
				{
					logEntry = mapper.readValue(line, LogEntry.class);
					parseLogDates(logEntry);
					if (logEntry.getQueryHash() != null) 
					{
						logEntries.add(logEntry);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new MongoDbLogReaderException("Unable to parse log file!");
		}
		return logEntries;
	}
	
	/**
	 * Private method used to actually parse the file into the map.
	 * 
	 * @return Map containing the query hash as a string and the query metadata
	 *         object the contains the details of the query.
	 * 
	 * @throws MongoDbLogReaderException if we are unable to parse the file into the
	 *                                   map.
	 */
	private static Map<String, QueryMetadata> parseLogFile(File logFile) throws MongoDbLogReaderException 
	{
		Map<String, QueryMetadata> uniqueQueries = new HashMap<String, QueryMetadata>();

		try {
			LogEntry logEntry = null;
			QueryMetadata qm = null;
			String queryHash = null;

			try (BufferedReader br = new BufferedReader(new FileReader(logFile))) 
			{
				String line;
				while ((line = br.readLine()) != null) 
				{
//					logEntry = mapper.readValue(line, LogEntry.class);
					logEntry = parseLogEntry(line);
					
					parseLogDates(logEntry);
					
					queryHash = logEntry.getQueryHash();
					if (queryHash != null) 
					{
						if (uniqueQueries.containsKey(queryHash)) 
						{
							qm = uniqueQueries.get(queryHash);
							qm.addOne(logEntry);
							uniqueQueries.replace(queryHash, qm);
						} else {
							qm = new QueryMetadata(logEntry);
							uniqueQueries.put(queryHash, qm);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new MongoDbLogReaderException("Unable to parse log file!");
		}
		return uniqueQueries;
	}	
	
	public static LogEntry parseLogEntry(String line) throws JsonMappingException, JsonProcessingException
	{
		ObjectMapper mapper = new ObjectMapper();

		LogEntry logEntry = mapper.readValue(line, LogEntry.class);
		return logEntry;
	}
}
