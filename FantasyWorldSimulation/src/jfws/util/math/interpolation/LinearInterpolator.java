package jfws.util.math.interpolation;

public class LinearInterpolator extends TwoValueInterpolator {

	@Override
	public double interpolate(double start, double end, double factor) {
		double diff = end - start;
		return start + factor * diff;
	}
}
