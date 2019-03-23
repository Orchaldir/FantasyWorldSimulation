package jfws.features.rainfall;

import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum RainfallLevel {
	VERY_WET(Color.BLUE),
	WET(Color.LIGHTBLUE),
	AVERAGE(Color.GREEN),
	DRY(Color.LIGHTGOLDENRODYELLOW),
	VERY_DRY(Color.GOLDENROD);

	public static final int NUMBER_OF_RANGES = RainfallLevel.values().length - 1;
	public static final double LEVEL_WIDTH = 1.0 / (double)NUMBER_OF_RANGES;

	@Getter
	private final Color color;

	public static RainfallLevel from(double rainfall) {
		int levelIndex = (int) Math.round(getLevelIndex(rainfall));

		return RainfallLevel.values()[levelIndex];
	}

	public static Color getColor(double rainfall) {
		double levelIndex = getLevelIndex(rainfall);
		int lowerLevelIndex = (int) levelIndex;
		Color lowerColor = RainfallLevel.values()[lowerLevelIndex].color;
		Color higherColor = RainfallLevel.values()[(int) Math.ceil(levelIndex)].color;

		return interpolate(levelIndex - lowerLevelIndex, 0, 1, lowerColor, higherColor);
	}

	public static double getLevelIndex(double rainfall) {
		double unlimitedLevelIndex = rainfall / LEVEL_WIDTH;
		return Math.min(Math.max(unlimitedLevelIndex, 0.0), NUMBER_OF_RANGES);
	}

	private static Color interpolate(double value, double min, double max, Color start, Color end) {
		double factor = (value - min) / (max - min);
		return start.interpolate(end, factor);
	}
}
