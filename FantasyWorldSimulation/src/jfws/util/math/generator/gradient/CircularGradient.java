package jfws.util.math.generator.gradient;

import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.interpolation.TwoValueInterpolator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CircularGradient implements Generator {

	private final TwoValueInterpolator interpolator;
	private final Point2d center;
	private final double radius;
	private final double valueAtCenter;
	private final double valueAtRadius;

	@Override
	public double generate(double x, double y) {
		final double distance = center.getDistanceTo(x, y);

		if(distance > radius) {
			return valueAtRadius;
		}

		final double factor = distance / radius;

		return interpolator.interpolate(valueAtCenter, valueAtRadius, factor);
	}
}
