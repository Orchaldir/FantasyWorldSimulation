package jfws.maps.sketch;

import java.io.File;
import java.io.IOException;

public interface SketchConverter {

	SketchMap load(File file) throws IOException;

	void save(File file, SketchMap map) throws IOException;
}
