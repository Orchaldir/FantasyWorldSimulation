package jfws.maps.sketch.terrain;

import javafx.scene.paint.Color;

public interface TerrainType {

	String NO_GROUP = "";

	String getName();

	String getGroup();

	Color getColor();

	double getBaseElevation();

	double getElevationVariation();

	boolean isDefault();
}
