package jfws.util.math.generator.gradient;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.interpolation.TwoValueInterpolator;

public class AbsoluteLinearGradient extends Gradient {

	private final Point2d direction;
	private final double base;

	public AbsoluteLinearGradient(TwoValueInterpolator interpolator, Point2d center, Point2d direction, double maxDistance, double valueAtCenter, double valueAtMax) {
		super(interpolator, maxDistance, valueAtCenter, valueAtMax);
		this.direction = direction.getNormalized();
		this.base = this.direction.getDotProduct(center);
	}

	@Override
	public double generate(double x, double y) {
		double distance = Math.abs(direction.getDotProduct(x, y) - base);

		return generate(distance);
	}
}
