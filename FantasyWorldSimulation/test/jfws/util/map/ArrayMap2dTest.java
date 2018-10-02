package jfws.util.map;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

class ArrayMap2dTest {

	private static final int WIDTH = 4;
	private static final int HEIGHT = 5;
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
		assertThat(MAP.getIndex(0, 0), is(equalTo(0)));
		assertThat(MAP.getIndex(2, 0), is(equalTo(2)));
		assertThat(MAP.getIndex(0, 3), is(equalTo(12)));
		assertThat(MAP.getIndex(2, 1), is(equalTo(6)));
	}

}