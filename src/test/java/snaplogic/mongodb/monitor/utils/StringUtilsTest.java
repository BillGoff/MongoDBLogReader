package snaplogic.mongodb.monitor.utils;


import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;


class StringUtilsTest {

	@Test
	void testEscapeDoubleQuote() {
		String testString = "IXSCAN { org_id: 1, _fts: \"text\", _ftsx: 1 }";
		String expectedString = "IXSCAN { org_id: 1, _fts: \\\"text\\\", _ftsx: 1 }";
		String afterTestString = StringUtils.escapeDoubleQuote(testString);
		
		assertTrue(expectedString.equals(afterTestString));
		
	}

}
