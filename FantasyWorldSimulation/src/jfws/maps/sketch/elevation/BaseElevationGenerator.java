package jfws.maps.sketch.elevation;

import jfws.maps.sketch.terrain.TerrainType;

public class BaseElevationGenerator implements ElevationGenerator {

	@Override
	public void prepare() {
		// nothing to prepare
	}

	@Override
	public double generate(TerrainType type) {
		return type.getBaseElevation();
	}
}
