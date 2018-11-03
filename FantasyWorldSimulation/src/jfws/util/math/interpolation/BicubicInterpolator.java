package jfws.util.math.interpolation;

public class BicubicInterpolator implements Interpolator2d {

	private CubicInterpolator cubicInterpolator = new CubicInterpolator();
	private double[] intermediateResult = new double[4];

	@Override
	public double interpolate(double[][] p, double x, double y) {
		intermediateResult[0] = cubicInterpolator.interpolate(p[0], y);
		intermediateResult[1] = cubicInterpolator.interpolate(p[1], y);
		intermediateResult[2] = cubicInterpolator.interpolate(p[2], y);
		intermediateResult[3] = cubicInterpolator.interpolate(p[3], y);

		return cubicInterpolator.interpolate(intermediateResult, x);
	}
}
