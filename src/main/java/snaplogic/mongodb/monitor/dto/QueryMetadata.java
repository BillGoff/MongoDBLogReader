package snaplogic.mongodb.monitor.dto;

public class QueryMetadata implements Comparable <QueryMetadata> {
	
	private Integer duration = Integer.valueOf(0);
	private LogEntry exampleEntry = null;
	private Integer highDuration = Integer.valueOf(0);
	private Integer lowDuration = Integer.valueOf(0);
	private Integer sum = Integer.valueOf(0);

	public QueryMetadata()
	{}

	public QueryMetadata(LogEntry entry)
	{
		this.exampleEntry = entry;
		this.sum = Integer.valueOf(1);
		this.duration = entry.getDuration();
		this.highDuration = duration;
		this.lowDuration = duration;
	}
	
	public void addOne(LogEntry entry)
	{
		this.setSum(this.getSum() + 1);
		this.setDuration(this.getDuration() + entry.getDuration());
	}

	public Integer calculateAverage()
	{
		return (duration.intValue() / sum.intValue());
	}

	@Override
	public int compareTo(QueryMetadata o) {
		if(this.calculateAverage() >= o.calculateAverage())
			return 0;
		else return 1;
	}

	public Integer getDuration() {
		return duration;
	}

	public LogEntry getExampleEntry() {
		return exampleEntry;
	}
	
	public Integer getHighDuration() {
		return highDuration;
	}
	
	public Integer getLowDuration() {
		return lowDuration;
	}

	public Integer getSum() {
		return sum;
	}
	
	public void setDuration(Integer duration) {
		this.duration = duration;
		if(duration > this.highDuration)
			this.highDuration = duration;
		if(duration < this.lowDuration)
			this.lowDuration = duration;
	}
	
	public void setExampleEntry(LogEntry exampleEntry) {
		this.exampleEntry = exampleEntry;
	}
	
	public void setHighDuration(Integer highDuration) {
		this.highDuration = highDuration;
	}
	
	public void setLowDuration(Integer lowDuration) {
		this.lowDuration = lowDuration;
	}
	
	public void setSum(Integer sum) {
		this.sum = sum;
	}

}
