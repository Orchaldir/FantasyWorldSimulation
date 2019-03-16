package jfws.features.temperature;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum TemperatureLevel {
	VERY_COLD(Color.WHITE),
	COLD(Color.CYAN),
	COOL(Color.BLUE),
	TEMPERATE(Color.GREEN),
	WARM(Color.YELLOW),
	HOT(Color.ORANGE),
	VERY_HOT(Color.ORANGE);

	public static final int NUMBER_OF_RANGES = TemperatureLevel.values().length - 1;
	public static final double LEVEL_WIDTH = 1.0 / (double)NUMBER_OF_RANGES;

	@Getter
	private final Color color;

	public static TemperatureLevel from(double temperature) {
		int levelIndex = (int) Math.round(getLevelIndex(temperature));

		return TemperatureLevel.values()[levelIndex];
	}

	public static Color getColor(double temperature) {
		double levelIndex = getLevelIndex(temperature);
		int lowerLevelIndex = (int) levelIndex;
		Color lowerColor = TemperatureLevel.values()[lowerLevelIndex].color;
		Color higherColor = TemperatureLevel.values()[(int) Math.ceil(levelIndex)].color;

		return interpolate(levelIndex - lowerLevelIndex, 0, 1, lowerColor, higherColor);
	}

	public static double getLevelIndex(double temperature) {
		double unlimitedLevelIndex = temperature / LEVEL_WIDTH;
		return Math.min(Math.max(unlimitedLevelIndex, 0.0), NUMBER_OF_RANGES);
	}

	private static Color interpolate(double value, double min, double max, Color start, Color end) {
		double factor = (value - min) / (max - min);
		return start.interpolate(end, factor);
	}
}
