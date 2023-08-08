package snaplogic.mongodb.monitor.dto;

public class Storage {

	class Data {
		private int bytesRead;
		private int bytesWritten;
		private int timeReadingMicros;
		private int timeWritingMicros;
		
		public int getBytesRead() {
			return bytesRead;
		}
		public int getBytesWritten() {
			return bytesWritten;
		}
		public int getTimeReadingMicros() {
			return timeReadingMicros;
		}
		public int getTimeWritingMicros() {
			return timeWritingMicros;
		}
		public void setBytesRead(int bytesRead) {
			this.bytesRead = bytesRead;
		}
		public void setBytesWritten(int bytesWritten) {
			this.bytesWritten = bytesWritten;
		}
		public void setTimeReadingMicros(int timeReadingMicros) {
			this.timeReadingMicros = timeReadingMicros;
		}
		public void setTimeWritingMicros(int timeWritingMicros) {
			this.timeWritingMicros = timeWritingMicros;
		}
	}

}
