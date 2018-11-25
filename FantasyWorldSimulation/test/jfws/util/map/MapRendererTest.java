package jfws.util.map;

import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InOrder;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.AdditionalMatchers.eq;
import static org.mockito.Mockito.*;

class MapRendererTest extends SharedTestData {

	public static final double WORLD_TO_SCREEN_FACTOR = 2.0;
	public static final double BORDER = 1;

	private ColorSelector<Integer> colorSelector;
	private Renderer renderer;
	private MapRenderer mapRenderer;
	private InOrder orderVerifier;

	@BeforeEach
	public void setUp() {
		colorSelector = mock(ColorSelector.class);
		renderer = mock(Renderer.class);
		mapRenderer = new MapRenderer(renderer, WORLD_TO_SCREEN_FACTOR, BORDER);
		orderVerifier = Mockito.inOrder(renderer);
	}

	@Test
	public void testBorderBetweenCells() {
		double border = 33.5;

		mapRenderer.setBorderBetweenCells(border);

		assertThat(mapRenderer.getBorderBetweenCells(), is(equalTo(border)));
	}

	@Test
	public void testConvertToWorld() {
		assertThat(mapRenderer.convertToWorld(46.8), is(equalTo(23.4)));
	}

	@Test
	public void testRender() throws OutsideMapException {
		prepareTestRender();

		mapRenderer.render(MAPPER, colorSelector);

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
		verifyScale(WORLD_TO_SCREEN_FACTOR);
		verifyRenderRow(CELL_Y_0);
		verifyRenderRow(CELL_Y_1);
		verifyRenderRow(CELL_Y_2);
		verifyRenderRow(CELL_Y_3);
		verifyRenderRow(CELL_Y_4);
		verifyScale(1.0 / WORLD_TO_SCREEN_FACTOR);
		orderVerifier.verifyNoMoreInteractions();
	}

	private void verifyScale(double scale) {
		orderVerifier.verify(renderer).setScale(scale);
	}

	private void verifyRenderRow(double y) {
		verifyRenderRectangle(CELL_X_0, y, Color.RED);
		verifyRenderRectangle(CELL_X_1, y, Color.GREEN);
		verifyRenderRectangle(CELL_X_2, y, Color.BLUE);
		verifyRenderRectangle(CELL_X_3, y, Color.YELLOW);
	}

	private void verifyRenderRectangle(double x, double y, Color color) {
		orderVerifier.verify(renderer).setFillColor(ArgumentMatchers.eq(color));
		orderVerifier.verify(renderer).renderRectangle(eq(x, ERROR), eq(y, ERROR), eq(RESOLUTION_X-BORDER, ERROR), eq(RESOLUTION_Y-BORDER, ERROR));
	}

	private void verifyColorSelector() {
		for(int i = 0; i < SIZE; i++) {
			verify(colorSelector).select(ArgumentMatchers.eq(i));
		}
	}

}