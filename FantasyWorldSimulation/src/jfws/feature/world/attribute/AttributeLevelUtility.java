package jfws.feature.world.attribute;

import javafx.scene.paint.Color;
import lombok.Getter;

public class AttributeLevelUtility<T extends AttributeLevel> {

	private final T[] featureLevels;
	private final int numberOfRanges;
	@Getter
	private final double levelWidth;

	public AttributeLevelUtility(T[] featureLevels) {
		this.featureLevels = featureLevels;
		numberOfRanges = featureLevels.length - 1;
		levelWidth = 1.0 / (double)numberOfRanges;
	}

	public double getLevelIndex(double value) {
		double unlimitedLevelIndex = value / levelWidth;
		return Math.min(Math.max(unlimitedLevelIndex, 0.0), numberOfRanges);
	}

	public T from(double value) {
		int levelIndex = (int) Math.round(getLevelIndex(value));

		return featureLevels[levelIndex];
	}

	public Color getColor(double value) {
		double levelIndex = getLevelIndex(value);
		int lowerLevelIndex = (int) levelIndex;
		Color lowerColor = featureLevels[lowerLevelIndex].getColor();
		Color higherColor = featureLevels[(int) Math.ceil(levelIndex)].getColor();

		return interpolate(levelIndex - lowerLevelIndex, 0, 1, lowerColor, higherColor);
	}
	private Color interpolate(double value, double min, double max, Color start, Color end) {
		double factor = (value - min) / (max - min);
		return start.interpolate(end, factor);
	}
}
