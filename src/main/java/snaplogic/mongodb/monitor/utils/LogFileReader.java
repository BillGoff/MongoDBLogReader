package snaplogic.mongodb.monitor.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import snaplogic.mongodb.monitor.dto.LogEntry;
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
			throws MongoDbLogReaderException 
	{
		Map<String, QueryMetadata> uniqueQueries = null;
		if (cli.hasOption(option)) {
			String fileName = cli.getOptionValue(option);

			if (logger.isDebugEnabled())
				logger.debug("Attempting to read log file (" + fileName + ")");

			if ((fileName != null) && (fileName.trim().length() > 0)) {
				File file = new File(fileName);
				if ((file.exists()) && (file.canRead())) {
					uniqueQueries = parseLogFile(file);

				} else if (file.exists())
					throw new MongoDbLogReaderException("Unable to read "
							+ "MongoDB log file.  Check your permissions for " + "file(" + fileName + ").");
				else
					throw new MongoDbLogReaderException("File(" + fileName + ") does NOT exist!");
			} else
				throw new MongoDbLogReaderException("No file name priveded.");
		} else
			throw new MongoDbLogReaderException("You must provide a file for " + "us to read the sbom from!");

		return uniqueQueries;
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
			ObjectMapper mapper = new ObjectMapper();

			try (BufferedReader br = new BufferedReader(new FileReader(logFile))) {
				String line;
				while ((line = br.readLine()) != null) {
					logEntry = mapper.readValue(line, LogEntry.class);
					queryHash = logEntry.getQueryHash();
					if (queryHash != null) {
						if (uniqueQueries.containsKey(queryHash)) {
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

}
