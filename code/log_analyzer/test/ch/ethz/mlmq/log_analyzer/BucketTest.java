package ch.ethz.mlmq.log_analyzer;

import org.junit.Assert;
import org.junit.Test;

import ch.ethz.mlmq.log_analyzer.Bucket;

public class BucketTest {

	private static final int HEAVY_LOAD_TIMES = 10000; // set to 10000000 for really heavy load

	@Test
	public void testBucketMean() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(10.0, b.mean(), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(10.0, b.mean(), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(15.0, b.mean(), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(20.0, b.mean(), 0.0000001);
	}

	@Test
	public void testBucketPercentileMean() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(10.0, b.percentile(50.0), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(10.0, b.percentile(50.0), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(10.0, b.percentile(50.0), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(17.5, b.percentile(50.0), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(25.0, b.percentile(50.0), 0.0000001);
	}

	@Test
	public void testBucketVariance() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(0.0, b.variance(), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(0.0, b.variance(), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(75.0, b.variance(), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(150, b.variance(), 0.0000001);
	}

	@Test
	public void testBucketPercentileMin() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(b.min(), b.percentile(0.01), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(b.min(), b.percentile(0.01), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(b.min(), b.percentile(0.01), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(b.min(), b.percentile(0.01), 0.0000001);
	}

	@Test
	public void testBucketMin() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(10.0, b.min(), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(10.0, b.min(), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(10.0, b.min(), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(10.0, b.min(), 0.0000001);
	}

	@Test
	public void testBucketPercentileMax() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(b.max(), b.percentile(99.99), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(b.max(), b.percentile(99.99), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(b.max(), b.percentile(99.99), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(b.max(), b.percentile(99.99), 0.0000001);
	}

	@Test
	public void testBucketMax() throws Exception {
		Bucket b = new Bucket();
		b.addValue(10);
		Assert.assertEquals(10.0, b.max(), 0.0000001);
		b.addValue(10);
		Assert.assertEquals(10.0, b.max(), 0.0000001);
		b.addValue(25);
		Assert.assertEquals(25.0, b.max(), 0.0000001);
		b.addValue(35);
		Assert.assertEquals(35.0, b.max(), 0.0000001);
	}

	@Test
	public void testBucketMeanHeavyLoad() throws Exception {
		Bucket b = new Bucket();

		for (int i = 0; i < 2 * HEAVY_LOAD_TIMES; i++)
			b.addValue(10);
		Assert.assertEquals(10.0, b.mean(), 0.0000001);

		for (int i = 0; i < HEAVY_LOAD_TIMES; i++)
			b.addValue(25);
		Assert.assertEquals(15.0, b.mean(), 0.0000001);

		for (int i = 0; i < HEAVY_LOAD_TIMES; i++)
			b.addValue(35);
		Assert.assertEquals(20.0, b.mean(), 0.0000001);
	}

}
