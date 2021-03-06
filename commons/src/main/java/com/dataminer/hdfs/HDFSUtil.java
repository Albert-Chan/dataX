package com.dataminer.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HDFSUtil {

	/**
	 * Gets the file system which the URI belongs to. The URI should be a full path
	 * contains schema and authority; Otherwise, the default configuration will be
	 * used.
	 * 
	 * @param uri
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static FileSystem getFileSystem(String uri) throws IOException, URISyntaxException {
		FileSystem fs = FileSystem.get(new URI(uri), new Configuration());
		return fs;
	}

	public static boolean exists(String uriPattern) throws IOException, URISyntaxException {
		FileStatus[] status = getFileSystem(uriPattern).globStatus(new Path(uriPattern));
		return status != null && status.length > 0;
	}

	public static boolean delete(String uri, boolean recursive) throws IOException, URISyntaxException {
		return getFileSystem(uri).delete(new Path(uri), recursive);
	}

	public static boolean delete(String uri) throws IOException, URISyntaxException {
		return delete(uri, true);
	}

	public static void deleteBatch(List<String> hdfsPath) throws IOException, URISyntaxException {
		for (String path : hdfsPath) {
			if (exists(path)) {
				delete(path);
			}
		}
	}

	public static void rename(String oldPath, String newPath) throws IOException, URISyntaxException {
		getFileSystem(oldPath).rename(new Path(oldPath), new Path(newPath));
	}

	public static void rename(Path oldPath, Path newPath) throws IOException, URISyntaxException {
		getFileSystem(oldPath.toString()).rename(oldPath, newPath);
	}

}
