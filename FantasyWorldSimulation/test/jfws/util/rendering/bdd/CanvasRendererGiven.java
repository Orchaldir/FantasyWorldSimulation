package jfws.util.rendering.bdd;


import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import jfws.util.rendering.CanvasRenderer;

import static org.mockito.Mockito.mock;

public class CanvasRendererGiven extends Stage<CanvasRendererGiven> {

	@ProvidedScenarioState
	private GraphicsContext graphicsContext;

	@ProvidedScenarioState
	private CanvasRenderer renderer;

	@ProvidedScenarioState
	private Color color;

	public CanvasRendererGiven a_canvas_renderer() {
		graphicsContext = mock(GraphicsContext.class);

		renderer = new CanvasRenderer(graphicsContext);

		return self();
	}

	public CanvasRendererGiven a_color() {
		color =  mock(Color.class);

		return self();
	}
}
