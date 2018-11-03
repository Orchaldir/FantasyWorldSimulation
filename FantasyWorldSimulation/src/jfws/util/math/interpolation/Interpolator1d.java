package jfws.util.math.interpolation;

public interface Interpolator1d {

	/*
	 * The array p contains the four values to interpolate.
	 * The parameter x is a values between 0 and 1 and defines an arbitrary position between the second and third point.
	 */
	double interpolate(double[] p, double x);

}
