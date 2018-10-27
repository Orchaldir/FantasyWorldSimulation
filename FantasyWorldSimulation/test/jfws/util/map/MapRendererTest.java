package jfws.util.map;

import jfws.util.rendering.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.verify;

class MapRendererTest extends SharedTestData {

	private Renderer renderer;
	private MapRenderer<Integer> mapRenderer;

	@BeforeEach
	void setup() {
		renderer = Mockito.mock(Renderer.class);
		mapRenderer = new MapRenderer<>(renderer, MAPPER);
	}

	private void verifyRenderRectangle(double x, double y) {
		verify(renderer).renderRectangle(eq(x, ERROR), eq(y, ERROR), eq(RESOLUTION_X, ERROR), eq(RESOLUTION_Y, ERROR));
	}

	private void verifyRenderRow(double y) {
		verifyRenderRectangle(CELL_X_0, y);
		verifyRenderRectangle(CELL_X_1, y);
		verifyRenderRectangle(CELL_X_2, y);
		verifyRenderRectangle(CELL_X_3, y);
	}

	@Test()
	void testRender(){
		mapRenderer.render();

		verifyRenderRow(CELL_Y_0);
		verifyRenderRow(CELL_Y_1);
		verifyRenderRow(CELL_Y_2);
		verifyRenderRow(CELL_Y_3);
		verifyRenderRow(CELL_Y_4);
	}

}