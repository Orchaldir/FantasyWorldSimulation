package jfws.util.math.generator.gradient;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.interpolation.TwoValueInterpolator;
import lombok.Getter;

@Getter
public class CircularGradient extends Gradient {

	private final Point2d center;

	public CircularGradient(TwoValueInterpolator interpolator, Point2d center, double maxDistance,
							double valueAtCenter, double valueAtMax) {
		super(interpolator, maxDistance, valueAtCenter, valueAtMax);
		this.center = center;
	}

	@Override
	public double generate(double x, double y) {
		double distance = center.getDistanceTo(x, y);

		return generate(distance);
	}

}
