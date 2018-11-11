package jfws.maps.sketch.elevation;

import javafx.scene.paint.Color;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeImpl;

public abstract class ElevationGeneratorTest {
	protected static final double BASE_ELEVATION_0 = 42.5;
	protected static final double BASE_ELEVATION_1 = -33.3;
	protected static final double ELEVATION_VARIATION_1 = 13.4;

	protected TerrainType type0 = new TerrainTypeImpl("", Color.PINK, BASE_ELEVATION_0, 0);
	protected TerrainType type1 = new TerrainTypeImpl("", Color.PINK, BASE_ELEVATION_1, ELEVATION_VARIATION_1);

	protected ElevationGenerator generator;
}
