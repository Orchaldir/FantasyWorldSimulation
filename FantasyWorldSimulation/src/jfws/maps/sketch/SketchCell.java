package jfws.maps.sketch;

import jfws.features.elevation.ElevationCell;
import jfws.maps.sketch.terrain.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SketchCell implements ElevationCell {

	private TerrainType terrainType;

	private double elevation;
}
