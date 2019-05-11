package jfws.util.math.interpolation;

import jfws.util.math.geometry.Point2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;

class TriangleInterpolatorTest {

	private static final Point2d A = new Point2d(-1.0, -1.0);
	private static final Point2d B = new Point2d(1.0, -1.0);
	private static final Point2d C = new Point2d(0.0, Math.sqrt(2.0));
	public static final double ERROR = 0.001;
	public static final double VALUE_A = 1.0;
	public static final double VALUE_B = 2.0;
	public static final double VALUE_C = 3.0;

	private TriangleInterpolator interpolator;

	@BeforeEach
	public void setUp() {
		interpolator = new TriangleInterpolator(A, B, C);
	}

	@Test
	public void testCorners() {
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, A), is(closeTo(VALUE_A, ERROR)));
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, B), is(closeTo(VALUE_B, ERROR)));
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, C), is(closeTo(VALUE_C, ERROR)));
	}

	@Test
	public void testEdges() {
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, Point2d.calculateCentroid(A, B)), is(closeTo(1.5, ERROR)));
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, Point2d.calculateCentroid(B, C)), is(closeTo(2.5, ERROR)));
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, Point2d.calculateCentroid(C, A)), is(closeTo(2.0, ERROR)));
	}

	@Test
	public void testCenter() {
		assertThat(interpolator.interpolate(VALUE_A, VALUE_B, VALUE_C, new Point2d(0,0)), is(closeTo(2.1213, ERROR)));
	}

}