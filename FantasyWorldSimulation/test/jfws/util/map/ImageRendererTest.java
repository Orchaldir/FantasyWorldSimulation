package jfws.util.map;

import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import jfws.util.rendering.ColorSelector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static jfws.util.map.SharedTestData.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ImageRendererTest {

	private Map2d<Integer> map;
	private ColorSelector<Integer> colorSelector;
	private ImageRenderer imageRenderer;

	@BeforeEach
	public void setUp() {
		map = mock(Map2d.class);
		colorSelector = mock(ColorSelector.class);

		imageRenderer = new ImageRenderer();
	}

	private Color getColor(int index) {
		int moduloIndex = index % 3;

		if(moduloIndex == 0) {
			return Color.RED;
		}
		else if(moduloIndex == 1) {
			return Color.GREEN;
		}

		return Color.BLUE;
	}

	@Test
	public void testRender() throws OutsideMapException {
		when(map.getCellMap()).thenReturn(MAP);

		doAnswer(invocation -> {
			int index = invocation.getArgument(0);
			return getColor(index);
		}).when(colorSelector).select(anyInt());

		WritableImage image = imageRenderer.render(map, colorSelector);

		PixelReader pixelReader = image.getPixelReader();

		assertNotNull(image);
		assertThat(image.getWidth(), is(equalTo((double)WIDTH)));
		assertThat(image.getHeight(), is(equalTo((double)HEIGHT)));

		for(int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				int index = MAP.getIndex(x, y);
				assertThat(pixelReader.getColor(x, y), is(equalTo(getColor(index))));
			}
		}
	}

}