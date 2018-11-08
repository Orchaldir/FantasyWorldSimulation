package jfws.util.math.interpolation;

public interface Interpolator2d {

	/*
	 * The array p contains the 16 values to interpolate.
	 * The first index is for x and the second index for y.
	 *
	 * The parameter x is a values between 0 and 1 and defines an arbitrary position between the second and third column.
	 *
	 * The parameter y is a values between 0 and 1 and defines an arbitrary position between the second and third row.
	 */
	double interpolate(double[][] p, double x, double y);

}
