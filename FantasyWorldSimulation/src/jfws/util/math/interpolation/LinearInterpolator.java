package jfws.util.math.interpolation;

public class LinearInterpolator implements Interpolator1d {

	public static final int START = 1;
	public static final int END = 2;

	@Override
	public double interpolate(double[] p, double factor) {
		return interpolate(p[START], p[END], factor);
	}

	public double interpolate(double start, double end, double factor) {
		double diff = end - start;
		return start + factor * diff;
	}
}
