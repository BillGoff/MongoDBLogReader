package snaplogic.mongodb.monitor.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import snaplogic.mongodb.monitor.dto.QueryDiff;
import snaplogic.mongodb.monitor.dto.QueryMetadata;

public class DiffUtils {

	/**
	 * (U) This method is used to produce the diff results for the two Query Maps passed in.
	 * @param uniqueQueriesFile1 Query Map results for the first logs.
	 * @param uniqueQueriesFile2 Query Map results for the second logs.
	 * @return Map of Query Diff objects showing the differences in performance of the two query map results.
	 */
	public static Map<String, QueryDiff> diffQueryResults(Map<String, QueryMetadata> uniqueQueriesFile1, 
			Map<String, QueryMetadata> uniqueQueriesFile2)
	{
		Map<String, QueryDiff> queryDiff = new HashMap<String, QueryDiff>();
		
		Set<String> keys = uniqueQueriesFile1.keySet();
		Iterator<String> iter = keys.iterator();
		QueryMetadata qm1 = null;
		QueryMetadata qm2 = null;
		String key = null;
		//String queryPlanHash1 = null;
		//String queryPlanHash2 = null;
		String queryPlanSummary1 = null;
		String queryPlanSummary2 = null;
		
		QueryDiff qd = null;
		
		while (iter.hasNext()) 
		{
			key = iter.next();
			qm1 = uniqueQueriesFile1.get(key);
			qm2 = uniqueQueriesFile2.get(key);
			if ((qm1 != null) && (qm2 != null))
			{
				//queryPlanHash1 = qm1.getExampleEntry().getPlanCacheKey();
				//queryPlanHash2 = qm2.getExampleEntry().getPlanCacheKey();
				//if (!(queryPlanHash1.equalsIgnoreCase(queryPlanHash2)))
				queryPlanSummary1 = qm1.getExampleEntry().getPlanSummary();
				queryPlanSummary2 = qm2.getExampleEntry().getPlanSummary();
				
				if ((queryPlanSummary1 != null) && (queryPlanSummary2 != null) && 
						(queryPlanSummary1.length() > 0) && (queryPlanSummary2.length() > 0))
				{
					if(! (queryPlanSummary1.equalsIgnoreCase(queryPlanSummary2)))
					{
						qd = new QueryDiff(qm1, qm2);
						queryDiff.put(key, qd);
					}
				}
			}
		}
		return queryDiff;
	}
	
	public static Integer calculateDiff(Integer first, Integer second)
	{
		return (second - first);
	}
}
