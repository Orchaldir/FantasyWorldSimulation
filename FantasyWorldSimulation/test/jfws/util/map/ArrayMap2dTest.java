package jfws.util.map;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ArrayMap2dTest {

	private static final int WIDTH = 4;
	private static final int HEIGHT = 5;

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
		assertThat(MAP.getSize(), is(equalTo(WIDTH*HEIGHT)));
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

}