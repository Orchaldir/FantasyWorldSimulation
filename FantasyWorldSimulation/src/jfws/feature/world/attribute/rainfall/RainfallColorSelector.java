package jfws.feature.world.attribute.rainfall;

import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;

public class RainfallColorSelector<T extends RainfallCell> implements ColorSelector<T> {

	@Override
	public String getName() {
		return "Rainfall";
	}

	@Override
	public Color select(T cell) {
		double rainfall = cell.getRainfall();

		return RainfallLevel.UTILITY.getColor(rainfall);
	}
}