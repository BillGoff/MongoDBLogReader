package snaplogic.mongodb.monitor.utils;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import snaplogic.mongodb.monitor.dto.LogEntry;
import snaplogic.mongodb.monitor.dto.LogEntryDate;

/**
 * This is the JUnit test class for the LogFileReader class.
 * @author bgoff
 * @since 5 Jul 2024
 */
class LogFileReaderTest 
{

	/**
	 * This method is used to test the reading of the log entry date from the log entry string.
	 */
	@Test
	void parseLogDateFromStringTest () 
	{
		String logEntryString = "{\"t\":{\"$date\":\"2024-06-26T03:34:48.639+00:00\"},\"s\":\"I\",  \"c\":\"COMMAND\",  \"id\":51803,   \"ctx\":\"conn11442093\",\"msg\":\"Slow query\",\"attr\":{\"type\":\"command\",\"ns\":\"assets.snodes\",\"command\":{\"findAndModify\":\"snodes\",\"query\":{\"_id\":{\"$oid\":\"66556b1f123fea2781b4f6fe\"},\"seqno\":6244},\"new\":false,\"update\":{\"$set\":{\"name\":\"DIM_Dynamics_D365_DTPDMIUSR46\",\"type\":\"Account\",\"owner\":\"sharathb@adobe.com\",\"u_by\":\"admin@snaplogic.com\",\"c_time\":{\"$date\":\"2024-05-28T05:26:55.309Z\"},\"u_time\":{\"$date\":\"2024-06-26T03:34:47.626Z\"},\"l_time\":{\"$date\":\"2024-06-26T03:35:47.626Z\"},\"p_id\":{\"$oid\":\"665468f2dff1d2a02d015bb2\"},\"metadata\":{\"class_label\":\"Dynamics 365 OAuth2 Account For Online\",\"update_reason\":\"auto_refresh\"},\"lease_info\":{\"msg\":\"Refresh oauth2 account update: cf2e0e68-3f49-4341-b8d1-28e1dd252a13\",\"xid\":\"1d6b628c-6ded-4474-a577-8466bc3bacd9\"},\"a_id\":\"cf2e0e68-3f49-4341-b8d1-28e1dd252a13\",\"seqno\":6245,\"acl\":[]}},\"fields\":{\"_id\":true},\"upsert\":false,\"writeConcern\":{\"j\":true,\"w\":2},\"lsid\":{\"id\":{\"$uuid\":\"6f3ed7a4-6af7-45f7-af9f-dc9ed86d1854\"}},\"txnNumber\":28,\"$clusterTime\":{\"clusterTime\":{\"$timestamp\":{\"t\":1719372887,\"i\":536}},\"signature\":{\"hash\":{\"$binary\":{\"base64\":\"gRo/rvL6zRQCHcikwND5Qfw8bWs=\",\"subType\":\"0\"}},\"keyId\":7336636497640030232}},\"$db\":\"assets\",\"$readPreference\":{\"mode\":\"primary\"}},\"planSummary\":\"IXSCAN { _id: 1 }\",\"keysExamined\":1,\"docsExamined\":1,\"nMatched\":1,\"nModified\":1,\"nUpserted\":0,\"keysInserted\":2,\"keysDeleted\":2,\"numYields\":0,\"queryHash\":\"D4556189\",\"planCacheKey\":\"DB8F58F0\",\"reslen\":239,\"locks\":{\"ParallelBatchWriterMode\":{\"acquireCount\":{\"r\":2}},\"FeatureCompatibilityVersion\":{\"acquireCount\":{\"w\":2}},\"ReplicationStateTransition\":{\"acquireCount\":{\"w\":3}},\"Global\":{\"acquireCount\":{\"w\":2}},\"Database\":{\"acquireCount\":{\"w\":2}},\"Collection\":{\"acquireCount\":{\"w\":2}},\"Mutex\":{\"acquireCount\":{\"r\":2}}},\"flowControl\":{\"acquireCount\":1,\"timeAcquiringMicros\":1},\"readConcern\":{\"level\":\"local\",\"provenance\":\"implicitDefault\"},\"writeConcern\":{\"w\":2,\"j\":true,\"wtimeout\":0,\"provenance\":\"clientSupplied\"},\"storage\":{},\"remote\":\"172.29.34.68:48588\",\"protocol\":\"op_msg\",\"durationMillis\":1012}}";
		String expectedString = "2024-06-26T03:34:48.639+00:00";

		try {
			LogEntryDate logEntryDate = LogFileReader.parseLogDateFromString(logEntryString);
			assertTrue(expectedString.equals(logEntryDate.getDateString()));						
		} 
		catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
}
