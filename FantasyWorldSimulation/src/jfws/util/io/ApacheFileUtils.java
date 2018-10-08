package jfws.util.io;

import java.io.File;
import java.io.IOException;

public class ApacheFileUtils implements FileUtils {
	@Override
	public String readWholeFile(File file) throws IOException {
		return org.apache.commons.io.FileUtils.readFileToString(file, "UTF-8");
	}
}
