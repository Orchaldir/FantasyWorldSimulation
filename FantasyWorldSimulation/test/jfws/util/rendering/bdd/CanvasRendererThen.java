package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Hidden;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class CanvasRendererThen extends Stage<CanvasRendererThen> {

	@ExpectedScenarioState
	private GraphicsContext graphicsContext;

	@ExpectedScenarioState
	private Color color;

	public void the_canvas_is_cleared(@Hidden double x, @Hidden double y,
									  @Hidden double width, @Hidden double height) {
		verify(graphicsContext).clearRect(x, y, width, height);
	}

	public void the_scale_of_the_canavas_is_set_to(double scale) {
		verify(graphicsContext).scale(scale, scale);
	}

	public void the_fill_color_is_set() {
		assertNotNull(color);

		verify(graphicsContext).setFill(color);
	}

	public void a_rectangle_is_rendered_on_the_canvas(@Hidden double x, @Hidden double y,
													  @Hidden double width, @Hidden double height) {
		verify(graphicsContext).fillRect(x, y, width, height);
	}
}
