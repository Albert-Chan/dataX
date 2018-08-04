package com.dataminer.acquisitor.ftp;

@FunctionalInterface
public interface FileChecker {
	public boolean check(String fileNme);
}
