package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.List;

import static jfws.maps.sketch.terrain.NullTerrainType.DEFAULT_GROUP;
import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;

public class SharedTestData {
	public static final String NAME_A = "A";
	public static final String NAME_B = "B";
	public static final String NAME_C = "C";

	public static final String GROUP = "G0";

	public static final TerrainType TERRAIN_TYPE_A = TerrainTypeImpl.builder().
			name(NAME_A).group(NO_GROUP).color(Color.RED).baseElevation(1).elevationVariation(2.5).build();
	public static final TerrainType TERRAIN_TYPE_B = TerrainTypeImpl.builder().
			name(NAME_B).group(NO_GROUP).color(Color.GREEN).build();
	public static final TerrainType TERRAIN_TYPE_C = TerrainTypeImpl.builder().
			name(NAME_C).group(GROUP).color(Color.BLUE).baseElevation(-1).build();

	public static final List<TerrainType> ALL_TYPES = Arrays.asList(TERRAIN_TYPE_A, TERRAIN_TYPE_B, TERRAIN_TYPE_C);
}
