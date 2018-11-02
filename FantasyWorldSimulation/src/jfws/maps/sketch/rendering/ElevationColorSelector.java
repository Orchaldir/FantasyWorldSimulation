package jfws.maps.sketch.rendering;

import javafx.scene.paint.Color;
import jfws.maps.sketch.SketchCell;
import jfws.util.rendering.ColorSelector;

import static jfws.maps.sketch.SketchCell.*;

public class ElevationColorSelector implements ColorSelector<SketchCell> {
	@Override
	public String getName() {
		return "Elevation";
	}

	@Override
	public Color select(SketchCell cell) {
		double elevation = cell.getElevation();

		if(elevation < MIN_ELEVATION || elevation > MAX_ELEVATION) {
			return Color.PINK;
		}
		else if(elevation < DEFAULT_ELEVATION) {
			return interpolate(elevation, MIN_ELEVATION, DEFAULT_ELEVATION, Color.DARKBLUE, Color.CYAN);
		}
		else if(elevation < HILL_ELEVATION) {
			return interpolate(elevation, DEFAULT_ELEVATION, HILL_ELEVATION, Color.LIGHTGREEN, Color.DARKGREEN);
		}

		return interpolate(elevation, HILL_ELEVATION, MAX_ELEVATION, Color.GREY, Color.WHITE);
	}

	private Color interpolate(double elevation, double min, double max, Color start, Color end) {
		double factor = (elevation - min) / (max - min);
		return start.interpolate(end, factor);
	}
}
