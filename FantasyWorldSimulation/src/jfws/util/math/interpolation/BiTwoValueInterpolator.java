package jfws.util.math.interpolation;

import lombok.AllArgsConstructor;

import static jfws.util.math.interpolation.TwoValueInterpolator.END;
import static jfws.util.math.interpolation.TwoValueInterpolator.START;

@AllArgsConstructor
public class BiTwoValueInterpolator implements Interpolator2d {

	private TwoValueInterpolator interpolator;

	@Override
	public double interpolate(double[][] p, double x, double y) {
		double start = interpolator.interpolate(p[START], y);
		double end = interpolator.interpolate(p[END], y);

		return interpolator.interpolate(start, end, x);
	}

	public static BiTwoValueInterpolator createBilinearInterpolator() {
		return new BiTwoValueInterpolator(new LinearInterpolator());
	}

	public static BiTwoValueInterpolator createBiCosineInterpolator() {
		return new BiTwoValueInterpolator(new CosineInterpolator());
	}
}
