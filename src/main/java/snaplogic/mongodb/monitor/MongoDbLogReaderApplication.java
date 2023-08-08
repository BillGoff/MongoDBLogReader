package snaplogic.mongodb.monitor;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import snaplogic.mongodb.monitor.dto.QueryMetadata;
import snaplogic.mongodb.monitor.utils.DateUtils;
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
		cliOptions.addOption(new Option("h", "help", false, "will print out the command line options."));
		cliOptions.addOption(new Option("f", "file", true, "Log file to read"));
		cliOptions.addOption(new Option("o", "output", true, "\"html\" output file"));
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
			} else {
				Map<String, QueryMetadata> uniqueQueries = LogFileReader.parseLogFile(cli, "file");

				Map<String, QueryMetadata> sortedMap = SortUtils.sortByDurationAverage(uniqueQueries, false);

				HtmlRenderer htmlRenderer = new HtmlRenderer();
					
				String html = htmlRenderer.renderDataTableHtml(sortedMap);
				
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
