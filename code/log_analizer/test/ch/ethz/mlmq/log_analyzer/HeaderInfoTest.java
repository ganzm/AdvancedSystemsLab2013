package ch.ethz.mlmq.log_analyzer;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import ch.ethz.mlmq.log_analyzer.HeaderInfo;

public class HeaderInfoTest {

	private HeaderInfo getHeaderInfo() {
		String testLogPath = "resource/test/test_log.log";
		File f = FileUtils.getFile(testLogPath);
		Assert.assertTrue(f.exists());

		HeaderInfo h = new HeaderInfo(FileUtils.getFile(testLogPath));
		return h;
	}

	@Test
	public void testCorrectBucketTime() throws Exception {
		HeaderInfo h = getHeaderInfo();
		long startBucketTime = h.getStartBucketTime();
		Assert.assertEquals(2011l, startBucketTime);
	}

}
