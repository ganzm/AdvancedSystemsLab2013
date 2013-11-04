package ch.ethz.mlmq.log_analizer;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class LogSplitTest {

	private LogAnalizer getLogAnalizer() {
		String testLogPath = "resource/test/test_log.log";
		File f = FileUtils.getFile(testLogPath);
		Assert.assertTrue(f.exists());

		LogAnalizer l = new LogAnalizer();
		l.addFile(testLogPath);
		return l;
	}

	@Test
	public void testCorrectBucketSize() throws Exception {
		LogAnalizer l = getLogAnalizer();
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2);
		Assert.assertEquals(2, b.size());
	}

	@Test
	public void testBucketStartTime() throws Exception {
		LogAnalizer l = getLogAnalizer();
		long startBucketTime = l.getStartBucketTime();
		Assert.assertEquals(2011l, startBucketTime);
	}
}
