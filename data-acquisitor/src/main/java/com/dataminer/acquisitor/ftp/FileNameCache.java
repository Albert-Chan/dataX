package com.dataminer.acquisitor.ftp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;

public class FileNameCache {
	private static Logger logger = LoggerFactory.getLogger(FileNameCache.class);
	private static final int MAX_CACHED_FILE_NAMES = 60;

	private String eldestFile;
	private LinkedList<String> fileNameList = new LinkedList<>();
	private HashMap<String, Long> standbyFiles = new HashMap<>();
	private FileChecker checker;

	public void setChecker(FileChecker checker) {
		this.checker = checker;
	}

	public void add(String lastFile) {
		this.fileNameList.push(lastFile);
		if (fileNameList.size() > MAX_CACHED_FILE_NAMES) {
			eldestFile = fileNameList.removeLast();
		}
	}

	/**
	 * Checks if the file has been download completely.
	 * 
	 * @param fileName
	 *            the name of the file to be checked
	 * @param size
	 *            the current size of the file
	 * @return true if the file is complete; false if we are not sure.
	 */
	public boolean checkStandby(String fileName, long size) {
		if ((checker == null || checker.check(fileName)) && !isRetrieved(fileName)) {
			logger.debug("Checking the size of " + fileName);
			Long previousSize = standbyFiles.put(fileName, size);
			if (previousSize != null && previousSize.longValue() == size) {
				standbyFiles.remove(fileName);
				return true;
			}
		}
		return false;
	}

	private boolean isRetrieved(String fileName) {
		return isExpired(fileName) || this.fileNameList.contains(fileName);
	}

	private boolean isExpired(String fileName) {
		if (eldestFile == null || eldestFile.isEmpty()) {
			return false;
		}
		// as the file name contains the timestamp, those elder than the eldestFile will
		// be considered as expired.
		return fileName.compareTo(eldestFile) <= 0;
	}

}
