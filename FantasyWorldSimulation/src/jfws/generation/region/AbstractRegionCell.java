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

	public final static ColorSelector<AbstractRegionCell> TERRAIN_COLOR_SELECTOR = cell -> cell.getTerrainType().getColor();

	public final static ColorSelector<AbstractRegionCell> ELEVATION_COLOR_SELECTOR = cell -> {
		if(cell.elevation < MIN_ELEVATION || cell.elevation > MAX_ELEVATION) {
			return Color.PINK;
		}

		double factor = (cell.elevation - MIN_ELEVATION) / (MAX_ELEVATION - MIN_ELEVATION);
		return Color.BLACK.interpolate(Color.WHITE, factor);
	};

	private TerrainType terrainType;

	private double elevation;
}
