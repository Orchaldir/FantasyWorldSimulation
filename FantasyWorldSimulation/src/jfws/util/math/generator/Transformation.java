package jfws.util.math.generator;

import lombok.Getter;

public class Transformation implements Generator {

	private final Generator generator;

	@Getter
	private final double minValue;

	@Getter
	private final double valueFactor;

	@Getter
	private final double resolution;

	public Transformation(Generator generator, double minValue, double maxValue, double resolution) {
		this.generator = generator;
		this.minValue = minValue;
		this.valueFactor = maxValue - minValue;
		this.resolution = resolution;
	}

	@Override
	public double generate(double x, double y) {
		return minValue + generator.generate(x / resolution, y / resolution) * valueFactor;
	}
}
