package jfws.util.math.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class Point2dTest {

	public static final double X0 = 1.0;
	public static final double X1 = 4.0;
	public static final double Y0 = 2.0;
	public static final double Y1 = 6.0;
	private Point2d point0;
	private Point2d point1;

	@BeforeEach
	public void setUp() {
		point0 = new Point2d(X0, Y0);
		point1 = new Point2d(X1, Y1);
	}

	@Nested
	class TestGetDistance {

		@Test
		public void testSamePoint() {
			assertThat(point0.getDistanceTo(point0), is(equalTo(0.0)));
			assertThat(point1.getDistanceTo(point1), is(equalTo(0.0)));
		}

		@Test
		public void testDifferentPoint() {
			assertThat(point0.getDistanceTo(point1), is(equalTo(5.0)));
			assertThat(point1.getDistanceTo(point0), is(equalTo(5.0)));
		}
	}

}