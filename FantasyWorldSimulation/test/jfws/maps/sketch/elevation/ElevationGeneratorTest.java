package jfws.maps.sketch.elevation;

import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeImpl;

public abstract class ElevationGeneratorTest {
	protected static final double BASE_ELEVATION_0 = 42.5;
	protected static final double BASE_ELEVATION_1 = -33.3;
	protected static final double ELEVATION_VARIATION_1 = 13.4;

	protected TerrainType type0 = TerrainTypeImpl.builder().baseElevation(BASE_ELEVATION_0).build();
	protected TerrainType type1 = TerrainTypeImpl.builder().baseElevation(BASE_ELEVATION_1).
			elevationVariation(ELEVATION_VARIATION_1).build();

	protected ElevationGenerator generator;
}
