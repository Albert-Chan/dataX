package com.dataminer.spark;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;

import com.dataminer.hdfs.HDFSUtil;
import com.hadoop.compression.lzo.LzopCodec;

public class RDDUtil {

	public static JavaRDD<String> union(JavaSparkContext ctx, List<String> paths) {
		JavaRDD<String> rdd = ctx.emptyRDD();
		for (String path : paths) {
			// TODO InvalidInputExcepton?
			rdd = rdd.union(ctx.textFile(path));
		}
		return rdd;
	}

	@SafeVarargs
	public static <R> JavaRDD<R> union(JavaSparkContext ctx, JavaRDD<R>... rdds) {
		JavaRDD<R> gatherRDD = ctx.emptyRDD();
		for (JavaRDD<R> rdd : rdds) {
			gatherRDD = gatherRDD.union(rdd);
		}
		return gatherRDD;
	}

	@SafeVarargs
	public static <K, V> JavaPairRDD<K, V> union(JavaSparkContext ctx, JavaPairRDD<K, V>... rdds) {
		JavaPairRDD<K, V> gatherRDD = JavaPairRDD.fromJavaRDD(ctx.emptyRDD());
		for (JavaPairRDD<K, V> rdd : rdds) {
			gatherRDD = gatherRDD.union(rdd);
		}
		return gatherRDD;
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
	private static boolean checkBeforeWrite(String hdfsPath, boolean overwrite) throws IOException, URISyntaxException {
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

	public static void toHDFSAsText(JavaRDD<?> rdd, String hdfsPath, boolean overwrite,
			Class<? extends CompressionCodec> codecClazz) throws IOException, URISyntaxException {
		// FIXME need a distributed lock to make the method as a transaction.
		if (checkBeforeWrite(hdfsPath, overwrite)) {
			if (null == codecClazz) {
				rdd.saveAsTextFile(hdfsPath);
			} else {
				rdd.saveAsTextFile(hdfsPath, codecClazz);
			}
		}
	}

	public static void toHDFSAsText(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, overwrite, null);
	}

	public static void toHDFSAsText(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, true, null);
	}

	public static void toHDFSAsSnappy(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, overwrite, SnappyCodec.class);
	}

	public static void toHDFSAsSnappy(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, true, SnappyCodec.class);
	}

	public static void toHDFSAsLZO(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, overwrite, LzopCodec.class);
	}

	public static void toHDFSAsLZO(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsText(rdd, hdfsPath, true, LzopCodec.class);
	}

	public static void toHDFSAsSequence(JavaRDD<?> rdd, String hdfsPath, boolean overwrite)
			throws IOException, URISyntaxException {
		if (checkBeforeWrite(hdfsPath, overwrite)) {
			rdd.saveAsObjectFile(hdfsPath);
		}
	}

	public static void toHDFSAsSequence(JavaRDD<?> rdd, String hdfsPath) throws IOException, URISyntaxException {
		toHDFSAsSequence(rdd, hdfsPath, true);
	}

	public static void toHive() {
	}

}
