package jfws.util.math.interpolation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static jfws.util.math.interpolation.Interpolator1d.ARRAY_SIZE;

@AllArgsConstructor
public class BiFourValueInterpolator implements Interpolator2d {

	@Getter
	private Interpolator1d interpolator;
	private final double[] intermediateResult = new double[ARRAY_SIZE];

	@Override
	public double interpolate(double[][] p, double x, double y) {
		intermediateResult[0] = interpolator.interpolate(p[0], y);
		intermediateResult[1] = interpolator.interpolate(p[1], y);
		intermediateResult[2] = interpolator.interpolate(p[2], y);
		intermediateResult[3] = interpolator.interpolate(p[3], y);

		return interpolator.interpolate(intermediateResult, x);
	}

	public static BiFourValueInterpolator createBicubicInterpolator() {
		return new BiFourValueInterpolator(new CubicInterpolator());
	}
}
