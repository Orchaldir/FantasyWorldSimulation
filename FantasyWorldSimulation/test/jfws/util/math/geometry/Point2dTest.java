package jfws.util.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
	private static final Point2d POINT_2 = new Point2d(X1, Y0);
	private static final Point2d POINT_3 = new Point2d(X0, Y1);

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
		public void testGetLengthWithZero() {
			assertLength(ZERO, 0.0);
		}

		@Test
		public void testGetLengthWithUnitX() {
			assertLength(UNIT_X, 1.0);
		}

		@Test
		public void testGetLengthWithUnitY() {
			assertLength(UNIT_Y, 1.0);
		}

		@Test
		public void testGetLengthWithDiff() {
			assertLength(DIFF, 5.0);
		}
	}

	@Nested
	class TestGetNormalized {

		@Test
		public void testGetNormalizedZero() {
			assertPoint(ZERO.getNormalized(), 0, 0);
		}

		@Test
		public void testGetNormalizedUnitX() {
			assertPoint(UNIT_X.getNormalized(), 1, 0);
		}

		@Test
		public void testGetNormalizedUnitY() {
			assertPoint(UNIT_Y.getNormalized(), 0, 1);
		}

		@Test
		public void testGetNormalizedDiff() {
			assertPoint(DIFF.getNormalized(), 0.6, 0.8);
		}
	}

	@Nested
	class TestGetAngle {
		@Test
		public void testGetAngle() {
			assertThat(POINT_0.getAngleTo(new Point2d(2.0, 2.0)), is(equalTo(0.0)));
			assertThat(POINT_0.getAngleTo(new Point2d(1.0, 3.0)), is(equalTo(Math.PI / 2.0)));
			assertThat(POINT_0.getAngleTo(new Point2d(1.0, 0.0)), is(equalTo(-Math.PI / 2.0)));
			assertThat(POINT_0.getAngleTo(new Point2d(0.0, 2.0)), is(equalTo(Math.PI)));
		}

		@Test
		public void testGetAngleWithLongerDistanceBetweenPoints() {
			assertThat(POINT_0.getAngleTo(new Point2d(20.0, 2.0)), is(equalTo(0.0)));
		}
	}

	@Nested
	class TestArithmetic {

		@Test
		public void testAdd() {
			assertThat(POINT_0.add(DIFF), is(equalTo(POINT_1)));
		}

		@Test
		public void testSubtract() {
			assertThat(POINT_1.subtract(DIFF), is(equalTo(POINT_0)));
		}

		@Test
		public void testMultiply() {
			Point2d result = POINT_1.multiply(2.5);
			assertThat(result.getX(), is(equalTo(10.0)));
			assertThat(result.getY(), is(equalTo(15.0)));
		}

		@Test
		public void testFromPolar() {
			assertPoint(POINT_0.fromPolar(Math.PI * 0.0, 1.0), 2.0, 2.0);
			assertPoint(POINT_0.fromPolar(Math.PI * 0.5, 2.0), 1.0, 4.0);
			assertPoint(POINT_0.fromPolar(Math.PI * 1.0, 3.0), -2.0, 2.0);
			assertPoint(POINT_0.fromPolar(Math.PI * 1.5, 4.0), 1.0, -2.0);
			assertPoint(POINT_0.fromPolar(Math.PI * 2.0, 5.0), 6.0, 2.0);
		}
	}

	@Nested
	class TestCalculateCentroid {

		@Test
		public void testCalculateCentroid() {
			Point2d centroid = Point2d.calculateCentroid(List.of(POINT_0, POINT_1, POINT_2, POINT_3));

			assertThat(centroid.getX(), is(equalTo(2.5)));
			assertThat(centroid.getY(), is(equalTo(4.0)));
		}

		@Test
		public void testCalculateCentroidWithEmptyList() {
			assertThrows(IllegalArgumentException.class, () ->  Point2d.calculateCentroid(Collections.emptyList()));
		}
	}

}