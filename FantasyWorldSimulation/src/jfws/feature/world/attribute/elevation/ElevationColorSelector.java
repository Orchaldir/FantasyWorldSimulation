package jfws.feature.world.attribute.elevation;

import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;

import static jfws.feature.world.attribute.elevation.ElevationCell.*;

public class ElevationColorSelector<T extends ElevationCell> implements ColorSelector<T> {

	public static final Color MIN_COLOR = Color.DARKBLUE;

	@Override
	public String getName() {
		return "Elevation";
	}

	@Override
	public Color select(T cell) {
		double elevation = cell.getElevation();

		if(elevation < MIN_ELEVATION) {
			return MIN_COLOR.darker();
		}
		else if(elevation > MAX_ELEVATION) {
			return Color.WHITE;
		}
		else if(elevation < DEFAULT_ELEVATION) {
			return interpolate(elevation, MIN_ELEVATION, DEFAULT_ELEVATION - 1,MIN_COLOR, Color.CYAN);
		}
		else if(elevation < HILL_ELEVATION) {
			return interpolate(elevation, DEFAULT_ELEVATION, HILL_ELEVATION - 1, Color.LIGHTGREEN, Color.DARKGREEN);
		}

		return interpolate(elevation, HILL_ELEVATION, MAX_ELEVATION, Color.GREY, Color.LIGHTGREY);
	}

	public Color interpolate(double elevation, double min, double max, Color start, Color end) {
		double factor = (elevation - min) / (max - min);
		return start.interpolate(end, factor);
	}
}