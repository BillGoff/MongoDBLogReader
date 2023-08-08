package snaplogic.mongodb.monitor.utils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.LinkedList;

import java.util.Map;
import java.util.Map.Entry;

import java.util.stream.Collectors;

import snaplogic.mongodb.monitor.dto.QueryMetadata;

/**
 * utility class used to sort the Map created by the log entries.
 * @author bgoff
 * @since 4 Auguest 2023
 */
public class SortUtils {

	private static final Comparator<QueryMetadata> bySum = (QueryMetadata obj1, QueryMetadata obj2) -> obj1.getSum()
			.compareTo(obj2.getSum());

	private static final Comparator<QueryMetadata> byDurAver = (QueryMetadata obj1, QueryMetadata obj2) -> obj1
			.calculateAverage().compareTo(obj2.calculateAverage());

	/**
	 * This method is used to sort the map passed in.
	 * 
	 * @param unsortMap    java.util.Map to be sorted.
	 * @param reverseOrder boolean a true value indicates that the new map returned
	 *                     will be sorted in the reverse order.
	 * @return Map new java.util.Map sorted by the count in the order identified by
	 *         the reverseOrder boolean passed in.
	 */
	public static Map<String, QueryMetadata> sortByCount(Map<String, QueryMetadata> unsortMap, boolean reverseOrder) {
		Map<String, QueryMetadata> sortedMap = null;

		if (reverseOrder)
			sortedMap = unsortMap.entrySet().stream()
					.sorted(Map.Entry.<String, QueryMetadata>comparingByValue(bySum).reversed()).collect(Collectors
							.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		else
			sortedMap = unsortMap.entrySet().stream().sorted(Map.Entry.<String, QueryMetadata>comparingByValue(bySum))
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1,
							LinkedHashMap::new));

		return sortedMap;
	}

	/**
	 * This method is used to sort the map passed in.
	 * 
	 * @param unsortMap    java.util.Map to be sorted.
	 * @param reverseOrder boolean a true value indicates that the new map returned
	 *                     will be sorted in the reverse order.
	 * @return Map new java.util.Map sorted by the average duration of the query in
	 *         the order identified by the reverseOrder boolean passed in.
	 */
	public static Map<String, QueryMetadata> sortByDurationAverage(Map<String, QueryMetadata> unsortMap,
			boolean reverseOrder) {
		Map<String, QueryMetadata> sortedMap = null;
		if (reverseOrder)
			sortedMap = unsortMap.entrySet().stream()
					.sorted(Map.Entry.<String, QueryMetadata>comparingByValue(byDurAver).reversed()).collect(Collectors
							.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
		else
			sortedMap = unsortMap.entrySet().stream()
					.sorted(Map.Entry.<String, QueryMetadata>comparingByValue(byDurAver)).collect(Collectors
							.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

		return sortedMap;
	}

}
