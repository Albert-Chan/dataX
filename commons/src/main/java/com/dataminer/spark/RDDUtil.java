package com.dataminer.spark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import com.dataminer.hdfs.HDFSUtil;
import com.hadoop.compression.lzo.LzopCodec;

public class RDDUtil {

	public static JavaRDD<String> union(JavaSparkContext ctx, List<String> paths) {
		JavaRDD<String> rdd = null;
		for (String path : paths) {
			// If the paths doesn't exist, InvalidInputException will be thrown. But it will
			// not be thrown in this union method call phase. union only puts the paths into
			// JobConf.
			if (Objects.isNull(rdd)) {
				rdd = ctx.textFile(path);
			} else {
				rdd = rdd.union(ctx.textFile(path));
			}
		}
		return rdd;
	}

	@SafeVarargs
	public static <R> JavaRDD<R> union(JavaRDD<R>... rdds) {
		JavaRDD<R> unitedRDD = rdds[0];
		for (int i = 1; i < rdds.length; i++) {
			unitedRDD = unitedRDD.union(rdds[i]);
		}
		return unitedRDD;
	}

	@SafeVarargs
	public static <K, V> JavaPairRDD<K, V> union(JavaPairRDD<K, V>... rdds) {
		JavaPairRDD<K, V> unitedRDD = rdds[0];
		for (int i = 1; i < rdds.length; i++) {
			unitedRDD = unitedRDD.union(rdds[i]);
		}
		return unitedRDD;
	}

	/**
	 * Checks if the HDFS path is available to be write data into.
	 * 
	 * @param hdfsPath
	 * @param overwrite
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	@NotDistributedTransaction
	private static boolean checkOnWrite(String hdfsPath, boolean overwrite) throws IOException, URISyntaxException {
		if (Objects.nonNull(hdfsPath) && !hdfsPath.isEmpty()) {
			if (HDFSUtil.exists(hdfsPath)) {
				if (overwrite) {
					HDFSUtil.delete(hdfsPath);
					return true;
				} else {
					return false;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	@NotDistributedTransaction
	public static void toHDFSAsText(JavaRDD<?> rdd, String hdfsPath, boolean overwrite,
			Class<? extends CompressionCodec> codecClazz) throws IOException, URISyntaxException {
		if (checkOnWrite(hdfsPath, overwrite)) {
			if (null == codecClazz) {
				rdd.saveAsTextFile(hdfsPath);
			} else {
				rdd.saveAsTextFile(hdfsPath, codecClazz);
			}
		}
	}

	@NotDistributedTransaction
	public static void toHDFSAsText(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, overwrite, null);
	}

	@NotDistributedTransaction
	public static void toHDFSAsText(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, true, null);
	}

	@NotDistributedTransaction
	public static void toHDFSAsSnappy(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, overwrite, SnappyCodec.class);
	}

	@NotDistributedTransaction
	public static void toHDFSAsSnappy(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, true, SnappyCodec.class);
	}

	@NotDistributedTransaction
	public static void toHDFSAsLZO(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, overwrite, LzopCodec.class);
	}

	@NotDistributedTransaction
	public static void toHDFSAsLZO(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, true, LzopCodec.class);
	}

	@NotDistributedTransaction
	public static void toHDFSAsSequence(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		if (checkOnWrite(hdfsPath, overwrite)) {
			rdd.saveAsObjectFile(hdfsPath);
		}
	}

	@NotDistributedTransaction
	public static void toHDFSAsSequence(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsSequence(rdd, hdfsPath, true);
	}

	public static void toHive() {
	}

}
