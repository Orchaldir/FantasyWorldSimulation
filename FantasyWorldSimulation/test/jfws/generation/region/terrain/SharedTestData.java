package jfws.generation.region.terrain;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class SharedTestData {
	public static final String NAME_A = "A";
	public static final String NAME_B = "B";
	public static final String NAME_C = "C";

	public static final TerrainType TERRAIN_TYPE_A = new TerrainTypeImpl(NAME_A, Color.RED);
	public static final TerrainType TERRAIN_TYPE_B = new TerrainTypeImpl(NAME_B, Color.GREEN);
	public static final TerrainType TERRAIN_TYPE_C = new TerrainTypeImpl(NAME_C, Color.BLUE);

	public static final List<TerrainType> ALL_TYPES = Arrays.asList(TERRAIN_TYPE_A, TERRAIN_TYPE_B, TERRAIN_TYPE_C);
}
