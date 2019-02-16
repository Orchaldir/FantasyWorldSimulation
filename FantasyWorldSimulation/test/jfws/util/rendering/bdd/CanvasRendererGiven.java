package jfws.util.rendering.bdd;


import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;
import jfws.util.rendering.CanvasRenderer;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class CanvasRendererGiven extends Stage<CanvasRendererGiven> {

	private static final double X0 = 0.0;
	private static final double Y0 = 0.0;
	private static final double X1 = 2.0;
	private static final double Y1 = 0.0;
	private static final double X2 = 2.0;
	private static final double Y2 = 3.0;
	private static final double X3 = 1.0;
	private static final double Y3 = 4.0;
	private static final double X4 = -1.0;
	private static final double Y4 = 2.0;

	public static final double[] COORDINATES_X = new double[] {X0, X1, X2, X3, X4};
	public static final double[] COORDINATES_Y = new double[] {Y0, Y1, Y2, Y3, Y4};

	@ProvidedScenarioState
	private GraphicsContext graphicsContext;

	@ProvidedScenarioState
	private CanvasRenderer renderer;

	@ProvidedScenarioState
	private Color color;

	@ProvidedScenarioState
	private List<Point2d> polygonPoints;

	public CanvasRendererGiven a_canvas_renderer() {
		graphicsContext = mock(GraphicsContext.class);

		renderer = new CanvasRenderer(graphicsContext);

		return self();
	}

	public CanvasRendererGiven a_color() {
		color =  mock(Color.class);

		return self();
	}

	public CanvasRendererGiven a_polygon() {
		polygonPoints = new ArrayList<>(5);

		polygonPoints.add(new Point2d(X0, Y0));
		polygonPoints.add(new Point2d(X1, Y1));
		polygonPoints.add(new Point2d(X2, Y2));
		polygonPoints.add(new Point2d(X3, Y3));
		polygonPoints.add(new Point2d(X4, Y4));

		return self();
	}
}
