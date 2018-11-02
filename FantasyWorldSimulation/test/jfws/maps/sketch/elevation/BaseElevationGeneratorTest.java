package jfws.maps.sketch.elevation;

import javafx.scene.paint.Color;
import jfws.maps.sketch.terrain.TerrainType;
import jfws.maps.sketch.terrain.TerrainTypeImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

class BaseElevationGeneratorTest {

	private static  final double BASE_ELEVATION = 42.5;
	private TerrainType type = new TerrainTypeImpl("", Color.PINK, BASE_ELEVATION, 2.4);
	private BaseElevationGenerator generator = new BaseElevationGenerator();

	@Test
	void testGenerate() {
		assertThat(generator.generate(type), is(equalTo(BASE_ELEVATION)));
	}

}