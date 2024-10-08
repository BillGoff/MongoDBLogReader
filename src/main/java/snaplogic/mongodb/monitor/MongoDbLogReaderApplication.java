package snaplogic.mongodb.monitor;

import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import snaplogic.mongodb.monitor.dto.LogEntry;
import snaplogic.mongodb.monitor.dto.QueryDiff;
import snaplogic.mongodb.monitor.dto.QueryMetadata;
import snaplogic.mongodb.monitor.utils.DateUtils;
import snaplogic.mongodb.monitor.utils.DiffUtils;
import snaplogic.mongodb.monitor.utils.HtmlRenderer;
import snaplogic.mongodb.monitor.utils.LogFileReader;
import snaplogic.mongodb.monitor.utils.SortUtils;

/**
 * 
 * @author bgoff
 *
 */
public class MongoDbLogReaderApplication {

	private static final Logger logger = LogManager.getLogger("MongoDBLogReader");

	/**
	 * (U) This method is used to create the valid options for command line usage.
	 * 
	 * @return Options for use when running via command line.
	 */
	private static Options createCliOptions() {
		Options cliOptions = new Options();
		cliOptions.addOption(new Option("d", "diff", false, "Option that tells us we will be doing a diff operation of two files being passed in."));
		cliOptions.addOption(new Option("h", "help", false, "will print out the command line options."));
		cliOptions.addOption(new Option("f", "file", true, "Log file to read"));
		cliOptions.addOption(new Option("g", "file2", true, "Log file to diff with the first log."));
		cliOptions.addOption(new Option("o", "output", true, "\"html\" output file"));
		cliOptions.addOption(new Option("v", "verbose", false, "Option that tell us we want to do a deep dive into the logs!"));
		return cliOptions;
	}

	/**
	 * Main.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		Date startDate = DateUtils.rightNowDate();

		logger.debug("Running MongoDB Logfile analyzer!");

		boolean failed = false;

		CommandLineParser cliParser = new DefaultParser();
		Options cliOptions = createCliOptions();
		boolean runningHelp = false;
		try {
			CommandLine cli = cliParser.parse(cliOptions, args);
			if (cli.hasOption("help")) {
				runningHelp = true;
				HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("help", cliOptions);
			} 
			else if (cli.hasOption("diff")) 
			{
				
				Map<String, QueryMetadata> uniqueQueriesFile1 = LogFileReader.parseLogFile(cli, "file");
				Map<String, QueryMetadata> uniqueQueriesFile2 = LogFileReader.parseLogFile(cli, "file2");

				Map<String, QueryDiff> queryDiff = DiffUtils.diffQueryResults(uniqueQueriesFile1, uniqueQueriesFile2);
				
				HtmlRenderer htmlRenderer = new HtmlRenderer();
				String html = htmlRenderer.renderDiffDataTableHtml(queryDiff);
				String outputFileName = HtmlRenderer.getOutputFileOption(cli);

				PrintWriter out = new PrintWriter(outputFileName);
				out.append(html);
				out.close();
				
			} 
			else if(cli.hasOption("verbose"))
			{
				List<LogEntry> logEntries = LogFileReader.deepDiveParseLogFile(cli, "file");
				
				Date sd = LogFileReader.getStartDate();
				Date ed = LogFileReader.getEndDate();

				HtmlRenderer htmlRenderer = new HtmlRenderer();

				String html = htmlRenderer.renderDataTableHtml(logEntries, sd, ed);
				String outputFileName = HtmlRenderer.getOutputFileOption(cli);

				PrintWriter out = new PrintWriter(outputFileName);
				out.append(html);
				out.close();				
			}
			else {
				Map<String, QueryMetadata> uniqueQueries = LogFileReader.parseLogFile(cli, "file");
				Map<String, QueryMetadata> sortedMap = SortUtils.sortByDurationAverage(uniqueQueries, false);

				HtmlRenderer htmlRenderer = new HtmlRenderer();
				
				Date sd = LogFileReader.getStartDate();
				Date ed = LogFileReader.getEndDate();
				
				String html = htmlRenderer.renderDataTableHtml(sortedMap, sd, ed);
				
				String outputFileName = HtmlRenderer.getOutputFileOption(cli);

				PrintWriter out = new PrintWriter(outputFileName);
				out.append(html);
				out.close();
			}
		} catch (Exception e) {
			failed = true;
			logger.error("Failed to analyze log file!", e);
			e.printStackTrace();
		} finally {
			if (logger.isInfoEnabled()) {
				StringBuilder msg = new StringBuilder(
						"It took " + DateUtils.computeDiff(startDate, DateUtils.rightNowDate()));
				if (failed)
					msg.append(" to fail, ");
				else
					msg.append(" to successfully, ");

				if (runningHelp)
					msg.append("to show the usage.");
				else
					msg.append("to analyze a log file.");
				logger.info(msg.toString());
			}
		}
	}

}
