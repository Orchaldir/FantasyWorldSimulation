package jfws.util.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class Point2dTest {

	private static final double DISTANCE_0 = 0.0;
	private static final double DISTANCE_1 = 5.0;
	private static final double ERROR = 0.0001;

	protected static final double DIFF_X = 3.0;
	protected static final double DIFF_Y = 4.0;

	private static final double X0 = 1.0;
	private static final double X1 = X0 + DIFF_X;
	private static final double Y0 = 2.0;
	private static final double Y1 = Y0 + DIFF_Y;

	private static final Point2d POINT_0 = new Point2d(X0, Y0);
	private static final Point2d POINT_1 = new Point2d(X1, Y1);

	private static final Point2d ZERO = new Point2d(0, 0);
	private static final Point2d UNIT_X = new Point2d(1, 0);
	private static final Point2d UNIT_Y = new Point2d(0, 1);
	private static final Point2d DIFF = new Point2d(DIFF_X, DIFF_Y);

	private void assertPoint(Point2d point, double x, double y) {
		assertThat(point.getX(), is(closeTo(x, ERROR)));
		assertThat(point.getY(), is(closeTo(y, ERROR)));
	}

	private void assertLength(Point2d point, double length) {
		assertThat(point.getLength(), is(closeTo(length, ERROR)));
	}

	@Test
	public void testGetter() {
		assertThat(POINT_0.getX(), is(equalTo(X0)));
		assertThat(POINT_0.getY(), is(equalTo(Y0)));
	}

	@Nested
	class TestGetDistanceWithPoint2d {

		@Test
		public void testSamePoint() {
			assertThat(POINT_0.getDistanceTo(POINT_0), is(equalTo(DISTANCE_0)));
			assertThat(POINT_1.getDistanceTo(POINT_1), is(equalTo(DISTANCE_0)));
		}

		@Test
		public void testDifferentPoint() {
			assertThat(POINT_0.getDistanceTo(POINT_1), is(equalTo(DISTANCE_1)));
			assertThat(POINT_1.getDistanceTo(POINT_0), is(equalTo(DISTANCE_1)));
		}
	}

	@Nested
	class TestGetDistance {

		@Test
		public void testSamePoint() {
			assertThat(POINT_0.getDistanceTo(X0, Y0), is(equalTo(DISTANCE_0)));
			assertThat(POINT_1.getDistanceTo(X1, Y1), is(equalTo(DISTANCE_0)));
		}

		@Test
		public void testDifferentPoint() {
			assertThat(POINT_0.getDistanceTo(X1, Y1), is(equalTo(DISTANCE_1)));
			assertThat(POINT_1.getDistanceTo(X0, Y0), is(equalTo(DISTANCE_1)));
		}
	}

	@Nested
	class TestGetDotProductWithPoint2d {
		@Test
		public void testSamePoint() {
			assertThat(POINT_0.getDotProduct(POINT_0), is(equalTo(5.0)));
			assertThat(POINT_1.getDotProduct(POINT_1), is(equalTo(52.0)));
		}

		@Test
		public void testDifferentPoint() {
			assertThat(POINT_0.getDotProduct(POINT_1), is(equalTo(16.0)));
			assertThat(POINT_1.getDotProduct(POINT_0), is(equalTo(16.0)));
		}
	}

	@Nested
	class TestGetDotProduct {
		@Test
		public void testSamePoint() {
			assertThat(POINT_0.getDotProduct(X0, Y0), is(equalTo(5.0)));
			assertThat(POINT_1.getDotProduct(X1, Y1), is(equalTo(52.0)));
		}

		@Test
		public void testDifferentPoint() {
			assertThat(POINT_0.getDotProduct(X1, Y1), is(equalTo(16.0)));
			assertThat(POINT_1.getDotProduct(X0, Y0), is(equalTo(16.0)));
		}
	}

	@Nested
	class TestGetLength {

		@Test
		public void testWithLengthZero() {
			assertLength(ZERO, 0.0);
		}

		@Test
		public void testUnitX() {
			assertLength(UNIT_X, 1.0);
		}

		@Test
		public void testUnitY() {
			assertLength(UNIT_Y, 1.0);
		}

		@Test
		public void testDiff() {
			assertLength(DIFF, 5.0);
		}
	}

	@Nested
	class TestGetNormalized {

		@Test
		public void testWithLengthZero() {
			assertPoint(ZERO.getNormalized(), 0, 0);
		}

		@Test
		public void testUnitX() {
			assertPoint(UNIT_X.getNormalized(), 1, 0);
		}

		@Test
		public void testUnitY() {
			assertPoint(UNIT_Y.getNormalized(), 0, 1);
		}
	}

}