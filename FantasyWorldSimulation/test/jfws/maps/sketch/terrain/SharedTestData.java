package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;

import java.util.Map;

import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;

public class SharedTestData {
	public static final String NAME_A = "A";
	public static final String NAME_B = "B";
	public static final String NAME_C = "C";

	public static final String GROUP = "G0";

	public static final double BASE_ELEVATION = 111.1;
	public static final double ELEVATION_VARIATION = 2.5;

	public static final Double[] NOISE_AMPLITUDES = { 0.0};

	public static final TerrainType TERRAIN_TYPE_A = TerrainTypeImpl.builder().
			name(NAME_A).group(NO_GROUP).color(Color.RED).baseElevation(1).elevationVariation(2.5).
			noiseAmplitudes(NOISE_AMPLITUDES).build();
	public static final TerrainType TERRAIN_TYPE_B = TerrainTypeImpl.builder().
			name(NAME_B).group(NO_GROUP).color(Color.GREEN).noiseAmplitudes(NOISE_AMPLITUDES).build();
	public static final TerrainType TERRAIN_TYPE_C = TerrainTypeImpl.builder().
			name(NAME_C).group(GROUP).color(Color.BLUE).baseElevation(-1).noiseAmplitudes(NOISE_AMPLITUDES).build();

	public static final Map<String,TerrainType> ALL_TYPES = Map.of(
			NAME_A, TERRAIN_TYPE_A,
			NAME_B, TERRAIN_TYPE_B,
			NAME_C, TERRAIN_TYPE_C);
}
