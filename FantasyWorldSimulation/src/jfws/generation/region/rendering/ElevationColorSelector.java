package jfws.generation.region.rendering;

import javafx.scene.paint.Color;
import jfws.generation.region.AbstractRegionCell;
import jfws.util.rendering.ColorSelector;

import static jfws.generation.region.AbstractRegionCell.MAX_ELEVATION;
import static jfws.generation.region.AbstractRegionCell.MIN_ELEVATION;

public class ElevationColorSelector implements ColorSelector<AbstractRegionCell> {
	@Override
	public String getName() {
		return "Elevation";
	}

	@Override
	public Color select(AbstractRegionCell cell) {
		double elevation = cell.getElevation();

		if(elevation < MIN_ELEVATION || elevation > MAX_ELEVATION) {
			return Color.PINK;
		}

		double factor = (elevation - MIN_ELEVATION) / (MAX_ELEVATION - MIN_ELEVATION);
		return Color.BLACK.interpolate(Color.WHITE, factor);
	}
}
