package snaplogic.mongodb.monitor.utils;

import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.jupiter.api.Test;

class DateUtilsTest {

	/**
	 * This test method is used to test the conversion of the MongoDB Log Dates to an actual java date.
	 */
	@Test
	void toDateTest() 
	{
		String dateString = "2024-06-26T03:34:48.639+00:00";
		String expectedString = "2024 06 25 23:34:48.639";

		try 
		{
		    Date startDate = DateUtils.toDate(dateString);
						
			assertTrue(expectedString.equals(DateUtils.toString(startDate)));
			
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

}
