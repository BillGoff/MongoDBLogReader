package snaplogic.mongodb.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;

import snaplogic.mongodb.monitor.dto.LogEntry;
import snaplogic.mongodb.monitor.dto.QueryDiff;
import snaplogic.mongodb.monitor.dto.QueryMetadata;
import snaplogic.mongodb.monitor.exceptions.MongoDbLogReaderException;

/**
 * This class is used to generate the HTML used to display the results of the
 * MongoDB Log file.
 * 
 * @author bgoff
 * @since 7 Aug 2023
 */
public class HtmlRenderer {

	private static final String beginHtmlFile = "beginHtml.txt";
	private static final String diffEndHtmlFile = "diffEndHtml.txt";
	private static final String endHtmlFile = "endHtml.txt";

	/**
	 * This method is used to get the contents of the input stream passed in.
	 * 
	 * @param is InputStream to read the contents of.
	 * @return String the contents of the input stream.
	 * @throws MongoDbLogReaderException if we are unable to get the contents of the
	 *                                   input stream.
	 */
	private static String getContents(InputStream is) throws MongoDbLogReaderException 
	{
		StringBuilder sb = new StringBuilder();

		try (InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
				BufferedReader reader = new BufferedReader(streamReader)) {

			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			throw new MongoDbLogReaderException("Unable to read the contents of the inputstream " + 
					"used to produce the HTML output!", e);
		}
		return sb.toString();
	}
	
	/**
	 * This method is used to get the output file name.
	 * @param cli CommandLine option to parse.
	 * @return
	 */
	public static String getOutputFileOption(CommandLine cli)
	{
		String outputFileName = "output.html";
		if (cli.hasOption("output")) {
			String temp = cli.getOptionValue("output");
			if ((temp != null) && (temp.length() > 0))
			{
				if(temp.endsWith(".html"))
					outputFileName = temp;
				else
					outputFileName = temp + ".html";
			}
		}
		return outputFileName;
	}

	/**
	 * This method is used to get an InputStream from the file identified by the
	 * file name passed in.
	 * 
	 * @param fileName String value of the file we want to get the input stream for.
	 * @return InputStream associated with the file Name passed in.
	 * @throws MongoDbLogReaderException if we are unable to produce an input stream
	 *                                   to the file whose name was passed in.
	 */
	private InputStream getFileFromResourceAsStream(String fileName) throws MongoDbLogReaderException 
	{
		// The class loader that loaded the class
		ClassLoader classLoader = getClass().getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		// the stream holding the file content
		if (inputStream == null) {
			throw new MongoDbLogReaderException("Unable to find the file (" + fileName + 
					") used to build the HTML output!");
		} else
			return inputStream;
	}
	
	/**
	 * This method is used to produce the HTML.
	 * 
	 * @param sortedMap Map containing the data we want to display.
	 * @return String the HTML with embedded JQuery DataTable for the data we want
	 *         to display.
	 * @throws MongoDbLogReaderException in the event we are unable to produce the
	 *                                   HTML for the sorted map passed in.
	 */
	public String renderDataTableHtml(Map<String, QueryMetadata> sortedMap) throws MongoDbLogReaderException 
	{
		InputStream is = getFileFromResourceAsStream(beginHtmlFile);
		StringBuilder sb = new StringBuilder(getContents(is));

		QueryMetadata qm = null;
		Set<String> keys = sortedMap.keySet();
		int counter = 0;
		Iterator<String> iter = keys.iterator();

		String cmd = "";
		String planSummary = "";
		LogEntry entry = null;
		
		while (iter.hasNext()) 
		{
			String key = iter.next();
			if (counter <= 0)
				sb.append("\"aaData\": [");
			else
				sb.append(",");

			qm = sortedMap.get(key);
			entry = qm.getExampleEntry();
			if(entry != null)
			{
				cmd = entry.getCmd();
				if (cmd == null)
					cmd = "";
				planSummary = entry.getPlanSummary();
				if(planSummary == null)
					planSummary = "";
			}
			else
			{
				cmd = "";
				planSummary = "";
			}
			
			sb.append("{ \"queryHash\": \"" + key + 
					"\", \"count\": \""+ qm.getSum() + 
					"\", \"averageDuration\": \"" + qm.calculateAverage() + 
					"\", \"highDuration\": \"" + qm.getHighDuration() + 
					"\", \"lowDuration\": \"" + qm.getLowDuration() + 
					"\", \"query\": \"" + StringUtils.escapeDoubleQuote(cmd) + 
					"\", \"planSummary\": \"" + StringUtils.escapeDoubleQuote(planSummary) + "\"}");
			
			counter++;
		}
		sb.append("], ");

		is = getFileFromResourceAsStream(endHtmlFile);
		sb.append(getContents(is));

		return sb.toString();

	}

	/**
	 * This method is used to produce the HTML for a Diff Query Report.
	 * 
	 * @param queryDiff Map containing the diff data we want to display.
	 * @return String the HTML with embedded JQuery DataTable for the data we want
	 *         to display.
	 * @throws MongoDbLogReaderException in the event we are unable to produce the
	 *                                   HTML for the query diff map passed in.
	 */	
	public String renderDiffDataTableHtml(Map<String, QueryDiff> queryDiff) throws MongoDbLogReaderException 
	{
		InputStream is = getFileFromResourceAsStream(beginHtmlFile);
		StringBuilder sb = new StringBuilder(getContents(is));

		QueryDiff qd = null;
		QueryMetadata qm1 = null;
		QueryMetadata qm2 = null;
		
		Set<String> keys = queryDiff.keySet();
		int counter = 0;
		Iterator<String> iter = keys.iterator();

		String cmd = "";
		String planSummary = "";
		String newPlanSummary = "";
		LogEntry entry = null;
		LogEntry entry2 = null;
		
		while (iter.hasNext()) 
		{
			String key = iter.next();
			if (counter <= 0)
				sb.append("\"aaData\": [");
			else
				sb.append(",");
			
			qd = queryDiff.get(key);
			qm1 = qd.getQuery1Md();
			qm2 = qd.getQuery2Md();
			
			entry = qm1.getExampleEntry();
			if(entry != null)
			{
				cmd = entry.getCmd();
				if (cmd == null)
					cmd = "";
				planSummary = entry.getPlanSummary();
				if(planSummary == null)
					planSummary = "";
			}
			else
			{
				cmd = "";
				planSummary = "";
			}
			entry2 = qm2.getExampleEntry();
			if(entry2 != null)
			{
				newPlanSummary = entry2.getPlanSummary();
				if(newPlanSummary == null)
					newPlanSummary = "";
			}
			else
				newPlanSummary = "";
			
			sb.append("{ \"queryHash\": \"" + key + 
					"\", \"orgCount\": \""+ qm1.getSum() + 
					"\", \"newCount\": \""+ qm2.getSum() + 
					"\", \"orgAverageDuration\": \"" + qm1.calculateAverage() + 
					"\", \"newAverageDuration\": \"" + qm2.calculateAverage() + 
					"\", \"orgHighDuration\": \"" + qm1.getHighDuration() + 
					"\", \"newHighDuration\": \"" + qm2.getHighDuration() + 
					"\", \"orgLowDuration\": \"" + qm1.getLowDuration() + 
					"\", \"newLowDuration\": \"" + qm2.getLowDuration() + 
					"\", \"query\": \"" + cmd + 
					"\", \"orgPlanSummary\": \"" + planSummary + 
					"\", \"newPlanSummary\": \"" + newPlanSummary + "\"}");

			counter++;
		}
		sb.append("], ");

		is = getFileFromResourceAsStream(diffEndHtmlFile);
		sb.append(getContents(is));

		return sb.toString();

	}
}
