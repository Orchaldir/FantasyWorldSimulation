package jfws.generation.map.terrain.type;

import jfws.generation.map.terrain.type.TerrainType;

import java.awt.*;

public class SharedTestData {
	public static final String NAME_A = "A";
	public static final String NAME_B = "B";
	public static final String NAME_C = "C";

	public static final TerrainType TERRAIN_TYPE_A = new TerrainTypeImpl(NAME_A, Color.RED);
	public static final TerrainType TERRAIN_TYPE_B = new TerrainTypeImpl(NAME_B, Color.GREEN);
	public static final TerrainType TERRAIN_TYPE_C = new TerrainTypeImpl(NAME_C, Color.BLUE);
}
