package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;
import jfws.features.elevation.noise.NoiseAmplitudeStorage;

public interface TerrainType extends NoiseAmplitudeStorage {

	String NO_GROUP = "";

	int HILL_NOISE_INDEX = 0;

	String getName();

	String getGroup();

	Color getColor();

	double getBaseElevation();

	double getElevationVariation();

	boolean isDefault();
}
