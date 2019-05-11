package jfws.util.math.interpolation;

import jfws.util.math.geometry.Point2d;

// See Barycentric coordinate system
public class TriangleInterpolator {

	private final double divider;
	private final double factor0;
	private final double factor1;
	private final double factor2;
	private final double factor3;
	private final Point2d c;

	public TriangleInterpolator(Point2d a, Point2d b, Point2d c) {
		this.c = c;
		factor0 = b.getY() - c.getY();
		factor1 = a.getX() - c.getX();
		factor2 = c.getX() - b.getX();
		factor3 = a.getY() - c.getY();
		divider =  factor0 * factor1 + factor2 * factor3;
	}

	public double interpolate(double valueA, double valueB, double valueC, Point2d point) {
		double diffToCX = point.getX() - c.getX();
		double diffToCY = point.getY() - c.getY();
		double weightA = ( factor0 * diffToCX + factor2 * diffToCY) / divider;
		double weightB = (-factor3 * diffToCX + factor1 * diffToCY) / divider;
		double weightC = 1 - weightA - weightB;

		return weightA * valueA + weightB * valueB + weightC * valueC;
	}
}
