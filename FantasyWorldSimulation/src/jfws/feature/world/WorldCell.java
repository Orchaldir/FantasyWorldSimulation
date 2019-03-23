package jfws.feature.world;

import jfws.features.elevation.ElevationCell;
import jfws.features.rainfall.RainfallCell;
import jfws.features.temperature.TemperatureCell;

public class WorldCell implements ElevationCell, TemperatureCell, RainfallCell {

	public static final int ELEVATION = 0;
	public static final int TEMPERATURE = 1;
	public static final int RAINFALL = 2;

	public final Double[] attributes = { 0.0, 0.0, 0.0 };

	@Override
	public double getElevation() {
		return attributes[ELEVATION];
	}

	@Override
	public void setElevation(double value) {
		attributes[ELEVATION] = value;
	}

	@Override
	public double getTemperature() {
		return attributes[TEMPERATURE];
	}

	@Override
	public void setTemperature(double value) {
		attributes[TEMPERATURE] = value;
	}

	@Override
	public double getRainfall() {
		return attributes[RAINFALL];
	}

	@Override
	public void setRainfall(double value) {
		attributes[RAINFALL] = value;
	}
}
