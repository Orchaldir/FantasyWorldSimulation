package jfws.maps.sketch;

import java.io.File;
import java.io.IOException;

public interface SketchConverter {

	void save(File file, SketchMap map) throws IOException;
}
