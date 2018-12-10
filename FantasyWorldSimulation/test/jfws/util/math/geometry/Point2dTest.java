package jfws.util.math.geometry;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class Point2dTest {

	public static final double DISTANCE_0 = 0.0;
	public static final double DISTANCE_1 = 5.0;

	private static final double X0 = 1.0;
	private static final double X1 = 4.0;
	private static final double Y0 = 2.0;
	private static final double Y1 = 6.0;
	private static final Point2d POINT_0 = new Point2d(X0, Y0);
	private static final Point2d POINT_1 = new Point2d(X1, Y1);

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

}