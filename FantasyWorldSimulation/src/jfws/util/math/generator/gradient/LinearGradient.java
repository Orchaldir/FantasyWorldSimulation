package jfws.util.math.generator.gradient;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.interpolation.TwoValueInterpolator;

public class LinearGradient extends Gradient {

	private final Point2d direction;
	private final double base;

	public LinearGradient(TwoValueInterpolator interpolator, Point2d center, Point2d direction, double maxDistance, double valueAtCenter, double valueAtMax) {
		super(interpolator, maxDistance, valueAtCenter, valueAtMax);
		this.direction = direction.getNormalized();
		this.base = this.direction.getDotProduct(center);
	}

	@Override
	public double generate(double x, double y) {
		double distance = direction.getDotProduct(x, y) - base;

		if(distance <= 0.0) {
			return  valueAtCenter;
		}

		return generate(distance);
	}
}
