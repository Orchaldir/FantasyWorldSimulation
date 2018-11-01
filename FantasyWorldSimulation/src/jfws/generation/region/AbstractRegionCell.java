package jfws.generation.region;

import javafx.scene.paint.Color;
import jfws.generation.region.terrain.TerrainType;
import jfws.util.rendering.ColorSelector;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AbstractRegionCell {

	public final static double DEFAULT_ELEVATION = 0.0;
	public final static double MIN_ELEVATION = -1000.0;
	public final static double MAX_ELEVATION =  1000.0;

	private TerrainType terrainType;

	private double elevation;
}
