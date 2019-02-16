package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.Hidden;
import javafx.scene.paint.Color;
import jfws.util.rendering.CanvasRenderer;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CanvasRendererWhen extends Stage<CanvasRendererWhen> {

	@ExpectedScenarioState
	private CanvasRenderer renderer;

	@ExpectedScenarioState
	private Color color;

	public void the_renderer_is_cleared(@Hidden double x, @Hidden double y, @Hidden double width, @Hidden double height) {
		renderer.clear(x, y, width, height);
	}

	public void the_scale_of_the_renderer_is_set_to(double scale) {
		renderer.setScale(scale);
	}

	public void the_render_color_is_set() {
		assertNotNull(color);

		renderer.setColor(color);
	}

	public void a_rectangle_is_rendered_with_the_renderer(@Hidden double x, @Hidden double y, @Hidden double width, @Hidden double height) {
		renderer.renderRectangle(x, y, width, height);
	}
}
