package jfws.feature.world;

import jfws.features.elevation.ElevationCell;
import jfws.features.temperature.TemperatureCell;

public class WorldCell implements ElevationCell, TemperatureCell {

	public static final int ELEVATION = 0;
	public static final int TEMPERATURE = 1;

	public final Double[] attributes = new Double[2];

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
}
