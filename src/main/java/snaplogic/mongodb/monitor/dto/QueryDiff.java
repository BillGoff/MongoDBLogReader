package snaplogic.mongodb.monitor.dto;

public class QueryDiff {

	private QueryMetadata query1Md = new QueryMetadata();
	public QueryMetadata getQuery1Md() {
		return query1Md;
	}

	public void setQuery1Md(QueryMetadata query1Md) {
		this.query1Md = query1Md;
	}

	public QueryMetadata getQuery2Md() {
		return query2Md;
	}

	public void setQuery2Md(QueryMetadata query2Md) {
		this.query2Md = query2Md;
	}

	private QueryMetadata query2Md = new QueryMetadata();
	
	public QueryDiff(QueryMetadata query1, QueryMetadata query2)
	{
		this.query1Md = query1;
		this.query2Md = query2;
	}
	
	
}
