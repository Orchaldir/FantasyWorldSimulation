package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

public class SharedTestData {
	public static final String NAME_A = "A";
	public static final String NAME_B = "B";
	public static final String NAME_C = "C";

	public static final String GROUP = "G0";

	public static final TerrainType TERRAIN_TYPE_A = new TerrainTypeImpl(NAME_A, Color.RED, 1, 2.5, 14.2);
	public static final TerrainType TERRAIN_TYPE_B = new TerrainTypeImpl(NAME_B, Color.GREEN, 0, 0, 0);
	public static final TerrainType TERRAIN_TYPE_C = new TerrainTypeImpl(NAME_C, GROUP, Color.BLUE, -1, 0.5, 0);

	public static final List<TerrainType> ALL_TYPES = Arrays.asList(TERRAIN_TYPE_A, TERRAIN_TYPE_B, TERRAIN_TYPE_C);
}
