package jfws.util.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class ApacheFileUtils implements FileUtils {

	// file

	@Override
	public String readWholeFile(File file) throws IOException {
		return org.apache.commons.io.FileUtils.readFileToString(file);
	}

	@Override
	public void writeWholeFile(File file, String text) throws IOException {
		org.apache.commons.io.FileUtils.writeStringToFile(file, text);
	}

	// path

	@Override
	public String getAbsolutePath(String relativePath) {
		return Paths.get(relativePath).toAbsolutePath().normalize().toString();
	}
}
