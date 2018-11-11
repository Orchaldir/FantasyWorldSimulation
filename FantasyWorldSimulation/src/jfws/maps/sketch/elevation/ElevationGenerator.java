package jfws.maps.sketch.elevation;

import jfws.maps.sketch.terrain.TerrainType;

public interface ElevationGenerator {

	void prepare();

	double  generate(TerrainType type);

}
