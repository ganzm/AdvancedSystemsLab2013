package ch.ethz.mlmq.log_analyzer;

import java.io.File;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class LogAnalizerTest {

	private static final String TEST_LOG1_PATH = "resource/test/test_log.log";
	private static final String TEST_LOG2_PATH = "resource/test/test2_log.log";
	private static final String TEST_LOG3_PATH = "resource/test/test3_log.log";

	private LogAnalizer getLogAnalizer() {
		String testLogPath = TEST_LOG1_PATH;
		File f = FileUtils.getFile(testLogPath);
		Assert.assertTrue(f.exists());

		LogAnalizer l = new LogAnalizer();
		l.addFile(f);
		return l;
	}

	@Test
	public void testCorrectBucketSize() throws Exception {
		LogAnalizer l = getLogAnalizer();
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 0);
		Assert.assertEquals(2, b.size());
	}

	@Test
	public void testCorrectBucketSizeWithMultipleFiles() throws Exception {
		LogAnalizer l = getLogAnalizer();
		l.addFile(FileUtils.getFile(TEST_LOG2_PATH));
		l.addFile(FileUtils.getFile(TEST_LOG3_PATH));
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 0);
		Assert.assertEquals(4, b.size());
	}

	@Test
	public void testCorrectBuckets() throws Exception {
		LogAnalizer l = getLogAnalizer();
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 0);
		Bucket b1 = b.get(0);
		Bucket b2 = b.get(1);

		Assert.assertEquals(4, b1.count());
		Assert.assertEquals(1, b2.count());
	}

	@Test
	public void testCorrectBucketsWithMultipleFiles() throws Exception {
		LogAnalizer l = getLogAnalizer();
		l.addFile(FileUtils.getFile(TEST_LOG2_PATH));
		l.addFile(FileUtils.getFile(TEST_LOG3_PATH));
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 0);
		Assert.assertEquals(4, b.size());

		Bucket b1 = b.get(0);
		Bucket b2 = b.get(1);
		Bucket b3 = b.get(2);
		Bucket b4 = b.get(3);

		Assert.assertEquals(12, b1.count());
		Assert.assertEquals(3, b2.count());
		Assert.assertEquals(0, b3.count());
		Assert.assertEquals(1, b4.count());
	}

	@Test
	public void testCutBucketsMini() throws Exception {
		LogAnalizer l = getLogAnalizer();
		l.addFile(FileUtils.getFile(TEST_LOG2_PATH));
		l.addFile(FileUtils.getFile(TEST_LOG3_PATH));
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 1);
		Assert.assertEquals(2, b.size());

		Bucket b1 = b.get(0);
		Bucket b2 = b.get(1);

		Assert.assertEquals(11, b1.count());
		Assert.assertEquals(3, b2.count());
	}

	@Test
	public void testCutBucketsHuge() throws Exception {
		LogAnalizer l = getLogAnalizer();
		l.addFile(FileUtils.getFile(TEST_LOG2_PATH));
		l.addFile(FileUtils.getFile(TEST_LOG3_PATH));
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 1000000000);
		Assert.assertEquals(0, b.size());
	}

	@Test
	public void testCutBuckets() throws Exception {
		LogAnalizer l = getLogAnalizer();
		l.addFile(FileUtils.getFile(TEST_LOG2_PATH));
		l.addFile(FileUtils.getFile(TEST_LOG3_PATH));
		ArrayList<Bucket> b = l.getBuckets("BRcvReq", 1000 * 60 * 2, 1000 * 60 * 1);

		Assert.assertEquals(1, b.size());
		Bucket b2 = b.get(0);

		Assert.assertEquals(3, b2.count());
	}

	@Test
	public void testBucketStartTime() throws Exception {
		LogAnalizer l = getLogAnalizer();
		long startBucketTime = l.getStartBucketTime();
		Assert.assertEquals(2011, startBucketTime);
	}

	@Test
	public void testBucketStartTimeWithMultipleFiles() throws Exception {
		LogAnalizer l = getLogAnalizer();
		l.addFile(FileUtils.getFile(TEST_LOG2_PATH));
		l.addFile(FileUtils.getFile(TEST_LOG3_PATH));
		long startBucketTime = l.getStartBucketTime();
		Assert.assertEquals(1022, startBucketTime);
	}

}
