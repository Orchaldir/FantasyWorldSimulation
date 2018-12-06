package jfws.maps.sketch.elevation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class BaseElevationGeneratorTest extends ElevationGeneratorTest {

	@BeforeEach
	public void setUp() {
		generator = new BaseElevationGenerator();
	}

	@Test
	public void testPrepare() {
		generator.prepare();
	}

	@Test
	public void testGenerate() {
		assertThat(generator.generate(type0), is(equalTo(BASE_ELEVATION_0)));
		assertThat(generator.generate(type1), is(equalTo(BASE_ELEVATION_1)));
	}

}