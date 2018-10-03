package jfws.util.map;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ArrayMap2dTest {

	private static final int WIDTH = 4;
	private static final int HEIGHT = 5;
	private static final int SIZE = WIDTH * HEIGHT;

	private static final int X0 = 0, Y0 = 0, INDEX0 = 0;
	private static final int X1 = 2, Y1 = 0, INDEX1 = 2;
	private static final int X2 = 0, Y2 = 3, INDEX2 = 12;
	private static final int X3 = 2, Y3 = 1, INDEX3 = 6;

	private static final IMap2d MAP = new ArrayMap2d(WIDTH, HEIGHT);

	@Test
	void testGetWidth() {
		assertThat(MAP.getWidth(), is(equalTo(WIDTH)));
	}

	@Test
	void testGetHeight() {
		assertThat(MAP.getHeight(), is(equalTo(HEIGHT)));
	}

	@Test
	void testGetSize() {
		assertThat(MAP.getSize(), is(equalTo(SIZE)));
	}

	@Test
	void testGetIndex() {
		assertThat(MAP.getIndex(X0, Y0), is(equalTo(INDEX0)));
		assertThat(MAP.getIndex(X1, Y1), is(equalTo(INDEX1)));
		assertThat(MAP.getIndex(X2, Y2), is(equalTo(INDEX2)));
		assertThat(MAP.getIndex(X3, Y3), is(equalTo(INDEX3)));
	}

	@Test
	void testGetX() {
		assertThat(MAP.getX(INDEX0), is(equalTo(X0)));
		assertThat(MAP.getX(INDEX1), is(equalTo(X1)));
		assertThat(MAP.getX(INDEX2), is(equalTo(X2)));
		assertThat(MAP.getX(INDEX3), is(equalTo(X3)));
	}

	@Test
	void testGetY() {
		assertThat(MAP.getY(INDEX0), is(equalTo(Y0)));
		assertThat(MAP.getY(INDEX1), is(equalTo(Y1)));
		assertThat(MAP.getY(INDEX2), is(equalTo(Y2)));
		assertThat(MAP.getY(INDEX3), is(equalTo(Y3)));
	}

	// isInsideForX()

	@Test
	void testIsInsideForX() {
		for(int x = 0; x < WIDTH; x++) {
			assertTrue(MAP.isInsideForX(x));
		}
	}

	@Test
	void testIsInsideForX_Left() {
		assertFalse(MAP.isInsideForX(-1));
	}

	@Test
	void testIsInsideForX_Right() {
		assertFalse(MAP.isInsideForX(WIDTH));
	}

	// isInsideForY()

	@Test
	void testIsInsideForY() {
		for(int y = 0; y < HEIGHT; y++) {
			assertTrue(MAP.isInsideForY(y));
		}
	}

	@Test
	void testIsInsideForY_Below() {
		assertFalse(MAP.isInsideForY(-1));
	}

	@Test
	void testIsInsideForY_Above() {
		assertFalse(MAP.isInsideForY(HEIGHT));
	}

	// isInsideForY()

	@Test
	void testIsInside() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				assertTrue(MAP.isInside(x, y));
			}
		}
	}

	@Test
	void testIsInside_Corners() {
		assertFalse(MAP.isInside(-1, -1));
		assertFalse(MAP.isInside(-1, HEIGHT));
		assertFalse(MAP.isInside(WIDTH, -1));
		assertFalse(MAP.isInside(WIDTH, HEIGHT));

	}

	@Test
	void testIsInside_Left() {
		for(int y = 0; y < HEIGHT; y++) {
			assertFalse(MAP.isInside(-1, y));
		}
	}

	@Test
	void testIsInside_Right() {
		for(int y = 0; y < HEIGHT; y++) {
			assertFalse(MAP.isInside(WIDTH, y));
		}
	}

	@Test
	void testIsInside_Below() {
		for(int x = 0; x < WIDTH; x++) {
			assertFalse(MAP.isInside(x, -1));
		}
	}

	@Test
	void testIsInside_Above() {
		for(int x = 0; x < WIDTH; x++) {
			assertFalse(MAP.isInside(x, HEIGHT));
		}
	}

	// isInside()

	@Test
	void testIsInsideWithIndex() {
		for(int index = 0; index < SIZE; index++) {
			assertTrue(MAP.isInside(index));
		}
	}

	@Test
	void testIsInsideWithIndex_Outside() {
		assertFalse(MAP.isInside(-1));
		assertFalse(MAP.isInside(SIZE));
	}

}