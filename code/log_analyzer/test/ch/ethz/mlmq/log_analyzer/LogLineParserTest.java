package ch.ethz.mlmq.log_analyzer;

import org.junit.Assert;
import org.junit.Test;

import ch.ethz.mlmq.log_analyzer.LogLine;
import ch.ethz.mlmq.log_analyzer.LogLineParser;

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
