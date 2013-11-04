package ch.ethz.mlmq.log_analizer;

import org.junit.Assert;
import org.junit.Test;

public class LogLineParserTest {
	@Test
	public void testLogLineParser() {
		LogLine l = LogLineParser.parseLogLine("15;3100;BSndResp;C[1]");
		Assert.assertEquals(15, l.getDuration());
		Assert.assertEquals(3100, l.getTimestamp());
		Assert.assertEquals("BSndResp", l.getName());
		Assert.assertEquals("C[1]", l.getAddition());
	}
}