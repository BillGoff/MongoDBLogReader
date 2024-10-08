package snaplogic.mongodb.monitor.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.cli.CommandLine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	private static final String diffEndHtmlFile = "diffEndHtml.txt";
	private static final String endDeepDiveHeadHtmlFile = "endDeepDiveHeadHtml.txt";
	private static final String endDeepDiveHtmlFile = "endDeepDiveHtml.txt";
	
	private static final String endHeadHtmlFile = "endHeadHtml.txt";
	private static final String endHtmlFile = "endHtml.txt";
	private static final String initialHtmlFile = "initialHtml.txt";
	
	private static final String msToTimeJsFile = "js/msToTime.js";
	
	private static final String multiSelectCssFile = "css/example-styles.css";
	private static final String multiSelectJsFile = "js/jquery.multi-select.min.js";
	private static final String myDataTableCssFile = "css/myDatatable.css";
	private static final String numbersWithCommasJsFile = "js/numbersWithCommas.js";
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
	 * This method is used to build the AAData String for the Deep Dive Option.
	 * @param logEntries List of LogEntries to pull the data from.
	 * @return String the AAData String that will be placed in the HTML page.
	 */
	private String buildAaData(List<LogEntry> logEntries)
	{
		StringBuilder sb = new StringBuilder("\"aaData\": [ ");
				
		for (LogEntry logEntry: logEntries)
		{
			if(sb.length() > 15)
				sb.append(", ");
			
			sb.append("{ " + logEntry.toJson() + "}");
		}
		sb.append(" ], ");

		return (sb.toString());
	}
	
	/**
	 * This method is used to build the AAData String for the standard option.
	 * @param sortedMap Map containing the queryhash key with its Query Metadata.
	 * @return String the AAData String that will be placed in the HTML page.
	 */
	private String buildAaData(Map<String, QueryMetadata> sortedMap)
	{
		StringBuilder sb = new StringBuilder();
		
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
		
		return (sb.toString());
	}

	/**
	 * This method is used to build the AAData String for the Diff of two different log files.
	 * @param queryDiff Map that contains the differences for each queryhash.
	 * @return String the AAData to place in the html page.
	 */
	private String buildDiffAaData(Map<String, QueryDiff> queryDiff)
	{
		StringBuilder sb = new StringBuilder();
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
					"\", \"diffAverageDuration\": \"" +
						DiffUtils.calculateDiff(qm1.calculateAverage(), qm2.calculateAverage()) +
					"\", \"orgHighDuration\": \"" + qm1.getHighDuration() + 
					"\", \"newHighDuration\": \"" + qm2.getHighDuration() + 
					"\", \"diffHighDuration\": \"" +
						DiffUtils.calculateDiff(qm1.getHighDuration(), qm2.getHighDuration()) + 
					"\", \"orgLowDuration\": \"" + qm1.getLowDuration() + 
					"\", \"newLowDuration\": \"" + qm2.getLowDuration() + 
					"\", \"diffLowDuration\": \"" +
						DiffUtils.calculateDiff(qm1.getLowDuration(), qm2.getLowDuration()) + 
					"\", \"query\": \"" + cmd + 
					"\", \"orgPlanSummary\": \"" + planSummary + 
					"\", \"newPlanSummary\": \"" + newPlanSummary + "\"}");

			counter++;
		}
		sb.append("], ");

		return (sb.toString());
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
	 * This method is used to produce the HTML for the Deep Dive of the log file.
	 * 
	 * @param logEntries List of log file entries to put in the HTML file.
	 * @param startDate Date that is the first date from the log file. ie the oldest.
	 * @param endDate Date that is the last date from the file.  Ie the most recent.
	 * @return String the HTML with embedded JQuery DataTable for the data we want
	 *         to display.
	 * @throws MongoDbLogReaderException in the event we are unable to produce the
	 *                                   HTML for the sorted map passed in.
	 * @throws JsonProcessingException 
	 */
	public String renderDataTableHtml(List<LogEntry> logEntries, Date startDate, Date endDate) throws MongoDbLogReaderException, JsonProcessingException 
	{
		StringBuilder sb = new StringBuilder(setUpInitial());
				
		sb.append("				" + buildAaData(logEntries) + "\n");

		InputStream is = getFileFromResourceAsStream(endDeepDiveHeadHtmlFile);
		sb.append(getContents(is));
		
		sb.append("	<body>\n");
		sb.append("	<div align=\"center\">" + DateUtils.toString(startDate) + " - " + DateUtils.toString(endDate));
		sb.append("	</div>\n");
		is = getFileFromResourceAsStream(endDeepDiveHtmlFile);
		sb.append(getContents(is));
		
		return sb.toString();

	}
	
	/**
	 * This method is used to produce the HTML.
	 * 
	 * @param sortedMap Map containing the data we want to display.
	 * @param startDate Date that is the first date from the log file. ie the oldest.
	 * @param endDate Date that is the last date from the file.  Ie the most recent.
	 * @return String the HTML with embedded JQuery DataTable for the data we want
	 *         to display.
	 * @throws MongoDbLogReaderException in the event we are unable to produce the
	 *                                   HTML for the sorted map passed in.
	 */
	public String renderDataTableHtml(Map<String, QueryMetadata> sortedMap, Date startDate, Date endDate)
			throws MongoDbLogReaderException 
	{
		StringBuilder sb = new StringBuilder(setUpInitial());

		sb.append("				" + buildAaData(sortedMap) + "\n");

		InputStream is = getFileFromResourceAsStream(endHeadHtmlFile);
		sb.append(getContents(is));
		
		sb.append("	<body>\n");
		sb.append("	<div align=\"center\">" + DateUtils.toString(startDate) + " - " + DateUtils.toString(endDate));
		sb.append("	</div>\n");
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
		StringBuilder sb = new StringBuilder(setUpInitial());
		
		sb.append("				" + buildDiffAaData(queryDiff) + "\n");
				
		InputStream is = getFileFromResourceAsStream(diffEndHtmlFile);
		sb.append(getContents(is));

		return sb.toString();

	}

	/**
	 * This method is used to setup the initial html page.  It includes all necessary java script and css.
	 * @return String the initial html page setup.
	 * @throws MongoDbLogReaderException in the event we are unable to load any of the files needed to setup the initial
	 * HTML page.
	 */
	private String setUpInitial() throws MongoDbLogReaderException
	{	
		InputStream is = getFileFromResourceAsStream(initialHtmlFile);
		StringBuilder sb = new StringBuilder(getContents(is));
		
		//Load My milliseconds to time javascript funciton.
		is = getFileFromResourceAsStream(msToTimeJsFile);
		sb.append("<script>\n" + getContents(is) + "</script>\n");
		
		is = getFileFromResourceAsStream(myDataTableCssFile);
		sb.append("<style type=\"text/css\">\n" + getContents(is) + "</style>\n");
		
		//Load My milliseconds to time javascript funciton.
		is = getFileFromResourceAsStream(numbersWithCommasJsFile);
		sb.append("<script>\n" + getContents(is) + "</script>\n");
				
		//Build JQuery Datatable.
		sb.append("<script>\n");
		sb.append("		$(document).ready(function(){ \n");
		sb.append("			oTable = $(\"#mongodbLogTable\").DataTable({\n" +
				"				responsive: true,\n" +
				"				colReorder: true,\n" +
				"				fixedHeader: true,\n" + 
				"				colReorder: true,\n" + 
				"				layout: { topStart: { buttons: ['colvis', 'pageLength'] }},\n");
		return (sb.toString());
	}
}
