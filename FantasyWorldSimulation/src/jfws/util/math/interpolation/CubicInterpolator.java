package jfws.util.math.interpolation;

public class CubicInterpolator implements Interpolator1d {
	@Override
	public double interpolate(double[] p, double x) {
		return p[1] + 0.5 * x * (p[2] - p[0] + x * (2.0 * p[0] - 5.0  *p[1] + 4.0 * p[2] - p[3] + x * (3.0 * (p[1] - p[2]) + p[3] - p[0])));
	}
}
