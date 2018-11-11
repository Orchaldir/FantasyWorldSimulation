package jfws.util.math.interpolation;

import static java.lang.Math.PI;

public class CosineInterpolator extends TwoValueInterpolator {

	@Override
	public double interpolate(double start, double end, double factor) {
		double mu2 = (1.0 - Math.cos(factor * PI)) / 2.0;
		return start * (1.0 - mu2) + end * mu2;
	}
}
