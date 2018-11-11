package jfws.util.math.interpolation;

public abstract class TwoValueInterpolator implements Interpolator1d {
	public static final int START = 1;
	public static final int END = 2;

	@Override
	public double interpolate(double[] p, double factor) {
		return interpolate(p[START], p[END], factor);
	}

	abstract double interpolate(double start, double end, double factor);
}
