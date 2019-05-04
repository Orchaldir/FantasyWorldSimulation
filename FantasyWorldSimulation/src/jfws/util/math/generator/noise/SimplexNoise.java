package jfws.util.math.generator.noise;

/*
 * A speed-improved simplex generate algorithm for 2D, 3D and 4D in Java.
 *
 * Based on example code by Stefan Gustavson (stegu@itn.liu.se).
 * Optimisations by Peter Eastman (peastman@drizzle.stanford.edu).
 * Better rank ordering method by Stefan Gustavson in 2012.
 *
 * This could be speeded up even further, but it's useful as it is.
 *
 * Version 2012-03-09
 *
 * This code was placed in the public domain by its original author,
 * Stefan Gustavson. You may use it as you see fit, but
 * attribution is appreciated.
 *
 */

import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;

public class SimplexNoise implements Generator {

	private static final Point2d[] GRADIENTS = {
			new Point2d(1, 1),
			new Point2d(-1, 1),
			new Point2d(1, -1),
			new Point2d(-1, -1),
			new Point2d(1, 0),
			new Point2d(-1, 0),
			new Point2d(1, 0),
			new Point2d(-1, 0),
			new Point2d(0, 1),
			new Point2d(0, -1),
			new Point2d(0, 1),
			new Point2d(0, -1)
	};

	private static final short[] PERMUTATION_TABLE = {151, 160, 137, 91, 90, 15,
			131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
			190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
			88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
			77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
			102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
			135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
			5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
			223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
			129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
			251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
			49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
			138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};

	// To remove the need for index wrapping, double the permutation table length
	private static final int P_SIZE = PERMUTATION_TABLE.length;
	private static final int PERM_SIZE = P_SIZE * 2;
	private static final short[] PERMUTATION_TWICE = new short[PERM_SIZE];
	private static final short[] PERMUTATION_MOD_12 = new short[PERM_SIZE];

	static {
		for (int i = 0; i < PERM_SIZE; i++) {
			PERMUTATION_TWICE[i] = PERMUTATION_TABLE[i & 255];
			PERMUTATION_MOD_12[i] = (short) (PERMUTATION_TWICE[i] % 12);
		}
	}

	// Skewing and unskewing factors
	private static final double F2 = 0.5 * (Math.sqrt(3.0) - 1.0);
	private static final double G2 = (3.0 - Math.sqrt(3.0)) / 6.0;

	@Override
	public double generate(double xin, double yin) {
		// Noise contributions from the three corners
		double n0;
		double n1;
		double n2;

		// Skew the input space to determine which simplex cell we're in
		double s = (xin + yin) * F2; // Hairy factor for 2D
		int i = floorFast(xin + s);
		int j = floorFast(yin + s);
		double t = (i + j) * G2;
		double originX = i - t; // Unskew the cell origin back to (x,y) space
		double originY = j - t;
		double x0 = xin - originX; // The x,y distances from the cell origin
		double y0 = yin - originY;

		// For the 2D case, the simplex shape is an equilateral triangle.
		// Determine which simplex we are in.

		// Offsets for second (middle) corner of simplex in (i,j) coords
		int i1;
		int j1;

		if (x0 > y0) {
			i1 = 1;
			j1 = 0;
		} // lower triangle, XY order: (0,0)->(1,0)->(1,1)
		else {
			i1 = 0;
			j1 = 1;
		} // upper triangle, YX order: (0,0)->(0,1)->(1,1)

		// A step of (1,0) in (i,j) means a step of (1-c,-c) in (x,y), and
		// a step of (0,1) in (i,j) means a step of (-c,1-c) in (x,y), where
		// c = (3-sqrt(3))/6
		double x1 = x0 - i1 + G2; // Offsets for middle corner in (x,y) unskewed coords
		double y1 = y0 - j1 + G2;
		double x2 = x0 - 1.0 + 2.0 * G2; // Offsets for last corner in (x,y) unskewed coords
		double y2 = y0 - 1.0 + 2.0 * G2;

		// Work out the hashed gradient indices of the three simplex corners
		int ii = i & 255;
		int jj = j & 255;
		int gi0 = PERMUTATION_MOD_12[ii + PERMUTATION_TWICE[jj]];
		int gi1 = PERMUTATION_MOD_12[ii + i1 + PERMUTATION_TWICE[jj + j1]];
		int gi2 = PERMUTATION_MOD_12[ii + 1 + PERMUTATION_TWICE[jj + 1]];

		// Calculate the contribution from the three corners
		double t0 = 0.5 - x0 * x0 - y0 * y0;

		if (t0 < 0) {
			n0 = 0.0;
		}
		else {
			t0 *= t0;
			n0 = t0 * t0 * GRADIENTS[gi0].getDotProduct(x0, y0);
		}

		double t1 = 0.5 - x1 * x1 - y1 * y1;

		if (t1 < 0) {
			n1 = 0.0;
		}
		else {
			t1 *= t1;
			n1 = t1 * t1 * GRADIENTS[gi1].getDotProduct(x1, y1);
		}

		double t2 = 0.5 - x2 * x2 - y2 * y2;

		if (t2 < 0){
			n2 = 0.0;
		}
		else {
			t2 *= t2;
			n2 = t2 * t2 * GRADIENTS[gi2].getDotProduct(x2, y2);
		}

		// Add contributions from each corner to get the final noise value.
		// The result is scaled to return values in the interval [-1,1].
		return 70.0 * (n0 + n1 + n2);
	}

	// This method is a *lot* faster than using (int)Math.floor(x)
	public static int floorFast(double x) {
		int xi = (int) x;
		return x < xi ? xi - 1 : xi;
	}
}