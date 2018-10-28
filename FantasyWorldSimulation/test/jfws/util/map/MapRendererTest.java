package jfws.util.map;

import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MapRendererTest extends SharedTestData {

	public static final double BORDER = 1;

	private ColorSelector<Integer> colorSelector;
	private Renderer renderer;
	private MapRenderer<Integer> mapRenderer;
	private InOrder orderVerifier;

	@BeforeEach
	void setup() {
		colorSelector = Mockito.mock(ColorSelector.class);
		renderer = Mockito.mock(Renderer.class);
		mapRenderer = new MapRenderer<>(colorSelector, renderer, MAPPER, BORDER);
		orderVerifier = Mockito.inOrder(renderer);
	}

	private void verifyRenderRectangle(double x, double y, Color color) {
		orderVerifier.verify(renderer).setFillColor(ArgumentMatchers.eq(color));
		orderVerifier.verify(renderer).renderRectangle(eq(x, ERROR), eq(y, ERROR), eq(RESOLUTION_X-BORDER, ERROR), eq(RESOLUTION_Y-BORDER, ERROR));
	}

	private void verifyRenderRow(double y) {
		verifyRenderRectangle(CELL_X_0, y, Color.RED);
		verifyRenderRectangle(CELL_X_1, y, Color.GREEN);
		verifyRenderRectangle(CELL_X_2, y, Color.BLUE);
		verifyRenderRectangle(CELL_X_3, y, Color.YELLOW);
	}

	@Test()
	void testRender() throws OutsideMapException {
		prepareTestRender();

		mapRenderer.render();

		verifyColorSelector();
		verifyRender();
	}

	private void prepareTestRender() {
		for(int row = 0; row < HEIGHT; row++) {
			int start = row * WIDTH;
			when(colorSelector.select(start)).thenReturn(Color.RED);
			when(colorSelector.select(start+1)).thenReturn(Color.GREEN);
			when(colorSelector.select(start+2)).thenReturn(Color.BLUE);
			when(colorSelector.select(start+3)).thenReturn(Color.YELLOW);
		}
	}

	private void verifyRender() {
		verifyRenderRow(CELL_Y_0);
		verifyRenderRow(CELL_Y_1);
		verifyRenderRow(CELL_Y_2);
		verifyRenderRow(CELL_Y_3);
		verifyRenderRow(CELL_Y_4);
		orderVerifier.verifyNoMoreInteractions();
	}

	private void verifyColorSelector() {
		for(int i = 0; i < SIZE; i++) {
			verify(colorSelector).select(ArgumentMatchers.eq(i));
		}
	}

}