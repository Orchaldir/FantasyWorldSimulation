package jfws.generation.region.elevation;

import jfws.generation.region.terrain.TerrainType;

public class BaseElevationGenerator implements ElevationGenerator {
	@Override
	public double generate(TerrainType type) {
		return type.getBaseElevation();
	}
}
