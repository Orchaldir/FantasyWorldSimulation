package jfws.util.math.geometry;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {

	private static final Point2d START = new Point2d(1.1, -2.5);
	private static final Point2d SIZE = new Point2d(4.4, 7.5);
	private static final Point2d END = START.add(SIZE);
	private static final Point2d CORNER1 =  new Point2d(5.5, -2.5);
	private static final Point2d CORNER3 =  new Point2d(1.1, 5.0);

	private static final Point2d INSIDE = new Point2d(3, 0);

	private static final Point2d OUTSIDE_LEFT = new Point2d(0, 0);
	private static final Point2d OUTSIDE_RIGHT = new Point2d(10, 0);
	private static final Point2d OUTSIDE_TOP = new Point2d(0, 10);
	private static final Point2d OUTSIDE_BOTTOM = new Point2d(0, -5);

	@Test
	public void testFromSize() {
		Rectangle rectangle = Rectangle.fromSize(SIZE);

		assertRectangle(rectangle, new Point2d(0,0), SIZE);
	}

	@Test
	public void testFromStartAndSize() {
		Rectangle rectangle = Rectangle.fromSize(START, SIZE);

		assertRectangle(rectangle, START, END);
	}

	@Test
	public void testFromStartAndEnd() {
		Rectangle rectangle = Rectangle.fromStartAndEnd(START, END);

		assertRectangle(rectangle, START, END);
	}

	private void assertRectangle(Rectangle rectangle, Point2d start, Point2d end) {
		assertThat(rectangle.getStart(), is(equalTo(start)));
		assertThat(rectangle.getEnd(), is(equalTo(end)));
		assertThat(rectangle.getSize(), is(equalTo(SIZE)));
	}

	@Nested
	class TestIsInside {

		private Rectangle rectangle;

		@BeforeEach
		public void setUp() {
			rectangle = Rectangle.fromSize(START, SIZE);
		}

		@Test
		public void testIsInside() {
			assertTrue(rectangle.isInside(INSIDE));
		}

		@Test
		public void testIsOutside() {
			assertFalse(rectangle.isInside(OUTSIDE_LEFT));
			assertFalse(rectangle.isInside(OUTSIDE_RIGHT));
			assertFalse(rectangle.isInside(OUTSIDE_TOP));
			assertFalse(rectangle.isInside(OUTSIDE_BOTTOM));
		}

		@Test
		public void testIsInsideWithCorners() {
			assertTrue(rectangle.isInside(START));
			assertTrue(rectangle.isInside(END));
		}

	}

	@Nested
	class TestGetCorners {

		private Rectangle rectangle;

		@BeforeEach
		public void setUp() {
			rectangle = Rectangle.fromSize(START, SIZE);
		}

		@Test
		public void testNumberOfCorners() {
			assertThat(rectangle.getCorners(), hasSize(4));
		}

		@Test
		public void testGetCorners() {
			assertThat(rectangle.getCorners(), contains(START, CORNER1, END, CORNER3));
		}
	}
}