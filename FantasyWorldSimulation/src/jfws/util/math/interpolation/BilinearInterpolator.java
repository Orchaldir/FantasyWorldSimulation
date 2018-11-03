package jfws.util.math.interpolation;

public class BilinearInterpolator implements Interpolator2d {

	private LinearInterpolator linearInterpolator = new LinearInterpolator();
	private double[] intermediateResult = new double[4];

	@Override
	public double interpolate(double[][] p, double x, double y) {
		intermediateResult[1] = linearInterpolator.interpolate(p[1], y);
		intermediateResult[2] = linearInterpolator.interpolate(p[2], y);

		return linearInterpolator.interpolate(intermediateResult, x);
	}
}
