package com.dataminer.hdfs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

public class UtilsTest {
	private static final String HDFS_BASE = "hdfs://192.168.0.1:8020/";

	@Test
	public void test() throws IOException, URISyntaxException {
		assertEquals(false, HDFSUtil.exists(HDFS_BASE + "/*"));
	}
}
