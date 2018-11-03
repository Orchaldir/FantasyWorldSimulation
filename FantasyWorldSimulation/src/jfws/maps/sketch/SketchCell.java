package jfws.maps.sketch;

import jfws.maps.sketch.terrain.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class SketchCell {

	public final static double MIN_ELEVATION =  -200.0;
	public final static double DEFAULT_ELEVATION = 0.0;
	public final static double HILL_ELEVATION =  100.0;
	public final static double MAX_ELEVATION =  1000.0;

	private TerrainType terrainType;

	private double elevation;
}