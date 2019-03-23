package jfws.features.rainfall;

import javafx.scene.paint.Color;
import jfws.features.temperature.TemperatureLevel;
import jfws.util.rendering.ColorSelector;

public class RainfallColorSelector<T extends RainfallCell> implements ColorSelector<T> {

	@Override
	public String getName() {
		return "Rainfall";
	}

	@Override
	public Color select(T cell) {
		double rainfall = cell.getRainfall();

		return RainfallLevel.getColor(rainfall);
	}
}