package ch.ethz.mlmq.log_analyzer;

import org.junit.Assert;
import org.junit.Test;

import ch.ethz.mlmq.log_analyzer.LogLine;
import ch.ethz.mlmq.log_analyzer.LogLineParser;

public class LogLineTest {
	@Test
	public void testBucketPosition() {
		LogLine l = LogLineParser.parseLogLine("15;100;BSndResp;C[1]");
		Assert.assertEquals(0, l.getBucketPosition(0, 1000));
		Assert.assertEquals(0, l.getBucketPosition(0, 500));
		Assert.assertEquals(0, l.getBucketPosition(0, 101));
		Assert.assertEquals(1, l.getBucketPosition(0, 100));
		Assert.assertEquals(1, l.getBucketPosition(0, 99));
		Assert.assertEquals(1, l.getBucketPosition(0, 51));
		Assert.assertEquals(2, l.getBucketPosition(0, 50));
		Assert.assertEquals(100, l.getBucketPosition(0, 1));

		Assert.assertEquals(0, l.getBucketPosition(50, 1050));
		Assert.assertEquals(0, l.getBucketPosition(50, 550));
		Assert.assertEquals(0, l.getBucketPosition(50, 151));
		Assert.assertEquals(0, l.getBucketPosition(50, 51));
		Assert.assertEquals(1, l.getBucketPosition(50, 50));
		Assert.assertEquals(1, l.getBucketPosition(50, 49));
		Assert.assertEquals(1, l.getBucketPosition(50, 26));
		Assert.assertEquals(2, l.getBucketPosition(50, 25));
		Assert.assertEquals(2, l.getBucketPosition(50, 24));
		Assert.assertEquals(50, l.getBucketPosition(50, 1));
	}
}
