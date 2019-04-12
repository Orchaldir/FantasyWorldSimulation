package jfws.feature.world;

import jfws.feature.world.attribute.AttributeCell;
import jfws.feature.world.attribute.elevation.ElevationCell;
import jfws.feature.world.attribute.magic.ManaCell;
import jfws.feature.world.attribute.rainfall.RainfallCell;
import jfws.feature.world.attribute.temperature.TemperatureCell;

public class WorldCell implements ElevationCell, TemperatureCell, RainfallCell, ManaCell, AttributeCell {

	public static final int ELEVATION = 0;
	public static final int TEMPERATURE = 1;
	public static final int RAINFALL = 2;
	public static final int MANA_LEVEL = 3;

	private final Double[] attributes = { 0.0, 0.0, 0.0, 0.0 };

	@Override
	public double getAttribute(int index) {
		return attributes[index];
	}

	@Override
	public void setAttribute(int index, double value) {
		attributes[index] = value;
	}

	@Override
	public void addToAttribute(int index, double value) {
		attributes[index] += value;
	}

	@Override
	public double getElevation() {
		return attributes[ELEVATION];
	}

	@Override
	public double getTemperature() {
		return attributes[TEMPERATURE];
	}

	@Override
	public double getRainfall() {
		return attributes[RAINFALL];
	}

	@Override
	public double getManaLevel() {
		return attributes[MANA_LEVEL];
	}
}
