package jfws.app.demo.biome;

import jfws.features.elevation.ElevationCell;
import jfws.features.temperature.TemperatureCell;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Cell implements ElevationCell, TemperatureCell {

	private double elevation;
	private double temperature;
}
