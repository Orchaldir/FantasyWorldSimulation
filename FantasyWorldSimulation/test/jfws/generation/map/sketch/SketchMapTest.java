package jfws.generation.map.sketch;

import jfws.util.map.OutsideMapException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.generation.map.terrain.type.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class SketchMapTest {

	public static final int WIDTH = 4;
	public static final int HEIGHT = 5;
	public static final int SIZE = WIDTH * HEIGHT;

	private SketchMap sketchMap;

	@BeforeEach
	void setup() {
		sketchMap = SketchMap.create(WIDTH, HEIGHT, TERRAIN_TYPE_A);
	}

	@Test
	void testCreate() throws OutsideMapException {
		assertThat(sketchMap, is(notNullValue()));
		assertThat(sketchMap.getWidth(), is(equalTo(WIDTH)));
		assertThat(sketchMap.getHeight(), is(equalTo(HEIGHT)));

		for(int i = 0; i < SIZE; i++) {
			assertThat(sketchMap.getCell(i), is(sameInstance(TERRAIN_TYPE_A)));
		}
	}
}