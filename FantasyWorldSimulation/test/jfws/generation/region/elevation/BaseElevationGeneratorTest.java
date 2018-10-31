package jfws.generation.region.elevation;

import javafx.scene.paint.Color;
import jfws.generation.region.terrain.TerrainType;
import jfws.generation.region.terrain.TerrainTypeImpl;
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