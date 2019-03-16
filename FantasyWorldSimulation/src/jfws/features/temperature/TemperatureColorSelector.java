package jfws.features.temperature;

import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;

public class TemperatureColorSelector<T extends TemperatureCell> implements ColorSelector<T> {

	@Override
	public String getName() {
		return "Temperature";
	}

	@Override
	public Color select(T cell) {
		double temperature = cell.getTemperature();

		return TemperatureLevel.getColor(temperature);
	}
}