package jfws.util.io;

import java.io.File;
import java.io.IOException;

public interface FileUtils {

	String readWholeFile(File file) throws IOException;

	void writeWholeFile(File file, String text) throws IOException;
}
