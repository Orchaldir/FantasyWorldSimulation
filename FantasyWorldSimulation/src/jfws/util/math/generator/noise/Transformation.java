package jfws.util.math.generator.noise;

import jfws.util.math.generator.Generator;
import lombok.Getter;

public class Transformation implements Generator {

	private final Generator generator;

	@Getter
	private final double baseValue;

	@Getter
	private final double valueFactor;

	@Getter
	private final double offsetX;

	@Getter
	private final double offsetY;

	@Getter
	private final double resolution;

	public Transformation(Generator generator, double minValue, double maxValue, double resolution) {
		this(generator, minValue, maxValue, 0, 0, resolution);
	}

	public Transformation(Generator generator, double minValue, double maxValue,
						  double offsetX, double offsetY, double resolution) {
		this.generator = generator;

		double difference = maxValue - minValue;
		double halfDifference = difference / 2.0;

		this.baseValue = minValue + halfDifference;
		this.valueFactor = halfDifference;

		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.resolution = resolution;
	}

	@Override
	public double generate(double x, double y) {
		double newX = (x + offsetX) / resolution;
		double newY = (y + offsetY) / resolution;
		return baseValue + generator.generate(newX, newY) * valueFactor;
	}
}
