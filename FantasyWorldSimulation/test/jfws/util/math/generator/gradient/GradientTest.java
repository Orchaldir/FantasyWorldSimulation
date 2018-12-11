package jfws.util.math.generator.gradient;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.interpolation.TwoValueInterpolator;

public class GradientTest {
	protected static final double MAX_DISTANCE = 10.0;
	protected static final double VALUE_AT_CENTER = 2.5;
	protected static final double VALUE_AT_MAX = 4.5;
	protected static final double VALUE_AT_HALF = (VALUE_AT_CENTER + VALUE_AT_MAX) / 2.0;

	protected static final double DIFF_SMALL = 3.0;
	protected static final double DIFF_LARGE = 4.0;

	protected static final double CENTER_X = 1.0;
	protected static final double CENTER_Y = 2.0;
	protected static final double DIAG_BEHIND_X = CENTER_X - DIFF_SMALL;
	protected static final double DIAG_BEHIND_Y = CENTER_Y - DIFF_LARGE;
	protected static final double DIAG_HALF_X = CENTER_X + DIFF_SMALL;
	protected static final double DIAG_HALF_Y = CENTER_Y + DIFF_LARGE;
	protected static final double DIAG_MAX_X = DIAG_HALF_X + DIFF_SMALL;
	protected static final double DIAG_MAX_Y = DIAG_HALF_Y + DIFF_LARGE;
	protected static final double DIAG_BEYOND_X = DIAG_MAX_X + DIFF_SMALL;
	protected static final double DIAG_BEYOND_Y = DIAG_MAX_Y + DIFF_LARGE;

	protected static final double BASE_RIGHT_X = CENTER_X + DIFF_LARGE;
	protected static final double BASE_RIGHT_Y = CENTER_Y - DIFF_SMALL;

	protected static final double BASE_LEFT_X = CENTER_X - DIFF_LARGE;
	protected static final double BASE_LEFT_Y = CENTER_Y + DIFF_SMALL;

	protected static final Point2d CENTER = new Point2d(CENTER_X, CENTER_Y);
	protected static final Point2d DIRECTION = new Point2d(DIFF_SMALL, DIFF_LARGE);

	protected TwoValueInterpolator interpolator;
}
