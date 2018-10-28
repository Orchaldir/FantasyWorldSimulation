package jfws.generation.region.terrain;

import javafx.scene.paint.Color;

public interface TerrainType {

	String getName();

	Color getColor();

	double getBaseElevation();

	double getElevationVariation();

	boolean isDefault();
}
