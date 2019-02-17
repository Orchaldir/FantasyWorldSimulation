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

	// given

	private void given_a_face_renderer() {
		renderer = mock(Renderer.class);

		faceRenderer = new FaceRenderer(renderer);
	}

	private void given_a_color_selector() {
		colorSelector = mock(ColorSelector.class);
		color = mock(Color.class);

		when(colorSelector.select(face)).thenReturn(color);
	}

	private void given_a_face(int id) {
		face = mock(Face.class);

		polygonPoints = new ArrayList<>();

		when(face.getId()).thenReturn(id);
		when(face.getPointsInCCW()).thenReturn(polygonPoints);
	}

	// verification

	private void verify_rendering_of_face_as_polygon(Color color, List<Point2d> points) {
		verify(renderer, times(1)).setColor(color);
		verify(renderer, times(1)).renderPolygon(points);
	}

	// tests

	@Test
	public void testRender() {
		given_a_face_renderer();
		given_a_face(2);
		given_a_color_selector();

		faceRenderer.render(face, colorSelector);

		verify_rendering_of_face_as_polygon(color, polygonPoints);
	}
}