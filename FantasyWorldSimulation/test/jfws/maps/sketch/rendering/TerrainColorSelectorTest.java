package jfws.maps.sketch.rendering;

import javafx.scene.paint.Color;
import jfws.maps.sketch.SketchCell;
import jfws.maps.sketch.terrain.TerrainType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TerrainColorSelectorTest {

	private static final Color COLOR = Color.CORAL;
	private TerrainType terrainType;
	private SketchCell sketchCell;
	private TerrainColorSelector colorSelector;

	@BeforeEach
	public void setUp() {
		terrainType = mock(TerrainType.class);
		sketchCell = mock(SketchCell.class);
		colorSelector = new TerrainColorSelector();
	}

	@Test
	void testGetName() {
		assertThat(colorSelector.getName(), is(equalTo(TerrainColorSelector.NAME)));
	}

	@Test
	void testSelect() {
		when(sketchCell.getTerrainType()).thenReturn(terrainType);
		when(terrainType.getColor()).thenReturn(COLOR);

		assertThat(colorSelector.select(sketchCell), is(equalTo(COLOR)));
	}
}