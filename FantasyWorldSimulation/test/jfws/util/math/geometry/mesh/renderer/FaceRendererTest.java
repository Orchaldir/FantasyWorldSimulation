package jfws.util.math.geometry.mesh.renderer;

import javafx.scene.paint.Color;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class FaceRendererTest {

	private Renderer renderer;
	private FaceRenderer faceRenderer;

	private ColorSelector colorSelector;
	private Color color;

	private Face face;
	private List<Point2d> polygonPoints;

	// tests

	@Test
	public void testRender() {
		createFaceRenderer();
		createFace(2);
		createColorSelector();

		faceRenderer.render(face, colorSelector);

		verifyRenderingOfFaceAsPolygon(color, polygonPoints);
	}

	// given

	private void createFaceRenderer() {
		renderer = mock(Renderer.class);

		faceRenderer = new FaceRenderer(renderer);
	}

	private void createColorSelector() {
		colorSelector = mock(ColorSelector.class);
		color = mock(Color.class);

		when(colorSelector.select(face)).thenReturn(color);
	}

	private void createFace(int id) {
		face = mock(Face.class);

		polygonPoints = new ArrayList<>();

		when(face.getId()).thenReturn(id);
		when(face.getPointsInCCW()).thenReturn(polygonPoints);
	}

	// verification

	private void verifyRenderingOfFaceAsPolygon(Color color, List<Point2d> points) {
		verify(renderer, times(1)).setColor(color);
		verify(renderer, times(1)).renderPolygon(points);
	}
}