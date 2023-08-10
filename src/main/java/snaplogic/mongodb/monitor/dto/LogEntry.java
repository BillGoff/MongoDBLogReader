package snaplogic.mongodb.monitor.dto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEntry {

//	private Date t;
//	private String s;
//	private String c;
//	private int id;
//	private String ctx;
	
	private String cmd;
    private Integer docsExamined = Integer.valueOf(0);
    
    private Integer duration = Integer.valueOf(0);

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	private Integer keysExamined = Integer.valueOf(0);

	@JsonProperty("msg")
	private String msg;
	private Integer nreturned = Integer.valueOf(0);

	//	private boolean cursorExhausted;
//	private int numYields;
	
	private String planCacheKey;
//	private int reslen;
//	private int duration;
//	
//	private Attr attr;
//	
//	private Storage storage;
	private String planSummary;

	private String queryHash;

	private String replanReason;

	public String getCmd() {
		return cmd;
	}

	public Integer getDocsExamined() {
		return docsExamined;
	}

	public int getKeysExamined() {
		return keysExamined;
	}

	public String getMsg() {
		return msg;
	}
	public Integer getNreturned() {
		return nreturned;
	}
    
	public String getPlanCacheKey() {
		return planCacheKey;
	}

	public String getPlanSummary() {
		return planSummary;
	}

	public String getQueryHash() {
		return queryHash;
	}
	
	public String getReplanReason() {
		return replanReason;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public void setDocsExamined(Integer docsExamined) {
		this.docsExamined = docsExamined;
	}

	public void setKeysExamined(int keysExamined) {
		this.keysExamined = keysExamined;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setNreturned(Integer nreturned) {
		this.nreturned = nreturned;
	}

	public void setPlanCacheKey(String planCacheKey) {
		this.planCacheKey = planCacheKey;
	}
	public void setPlanSummary(String planSummary) {
		this.planSummary = planSummary;
	}

	public void setQueryHash(String queryHash) {
		this.queryHash = queryHash;
	}

	public void setReplanReason(String replanReason) {
		this.replanReason = replanReason;
	}
	
	/**
	 * (U) Method used to printout the values of this Class.
	 *
	 * @return String nice readable string value of this class.
	 */
	@Override
	public String toString()
	{
		return (toString(""));
	}
	
	/**
	 * (U) Convenience method to make the string printed out indent as intended.
	 *
	 * @param tabs String value for the tabs used for indentation.
	 * @return String nice readable string value of this class.
	 */
	public String toString(String tabs)
	{
		StringBuilder sb = new StringBuilder();

		sb.append(tabs + "keysExamined: " + getKeysExamined() + "\n");
		sb.append(tabs + "docsExamined: " + getDocsExamined() + "\n");
		sb.append(tabs + "queryHash: "    + getQueryHash() + "\n");
		sb.append(tabs + "planCacheKey: " + getPlanCacheKey() + "\n");
		sb.append(tabs + "plannedSummary: " + getPlanSummary() + "\n");
		sb.append(tabs + "msg: " + getMsg() + "\n");
		
		return (sb.toString());
	}

	@JsonProperty("attr")
	private void unpackNameFromNestedObject(Map<String, Object> attr) {
		try
		{
			Object temp = attr.get("command");
			
			if(temp != null)
				this.setCmd(temp.toString());
			
			temp = attr.get("docsExamined");
			if ((temp != null) && (temp instanceof Integer))
				this.setDocsExamined((Integer) temp);
			
			temp = attr.get("durationMillis");
			if ((temp != null) && (temp instanceof Integer))
				this.setDuration((Integer) temp);
			
			temp = attr.get("keysExamined");
			if ((temp != null) && (temp instanceof Integer))
				this.setKeysExamined((Integer) temp);
			
			temp = attr.get("nreturned");
			if ((temp != null) && (temp instanceof Integer))
				this.setNreturned((Integer) temp);
				
			temp = attr.get("planSummary");
			if ((temp != null) && (temp instanceof String))
				this.setPlanSummary((String) temp);
			
			temp = attr.get("replanReason");
			if ((temp != null) && (temp instanceof String))
				this.setReplanReason((String) temp);
			
			temp = attr.get("queryHash");
			if ((temp != null) && (temp instanceof String))
				this.setQueryHash((String) temp);

			temp = attr.get("planCacheKey");
			if ((temp != null) && (temp instanceof String))
				this.setPlanCacheKey((String) temp);
			
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
