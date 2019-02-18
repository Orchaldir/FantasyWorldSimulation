package jfws.util.math.geometry.mesh.renderer;

import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.rendering.ColorSelector;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;

class MeshRendererTest {

	@Test
	public void testRenderFaces() {
		given_a_mesh_renderer();
		given_a_color_selector();
		given_a_mesh(3);

		meshRenderer.renderFaces(mesh, colorSelector);

		verify_reset_of_color_selector();
		verify_rendering_of_faces(3);
	}

	@Test
	public void testRenderNoFaces() {
		given_a_mesh_renderer();
		given_a_color_selector();
		given_a_mesh(0);

		meshRenderer.renderFaces(mesh, colorSelector);

		verify_reset_of_color_selector();
		verify_rendering_of_no_faces();
	}

	//

	private FaceRenderer faceRenderer;
	private MeshRenderer meshRenderer;
	private ColorSelector colorSelector;
	private Mesh mesh;
	private List<Face> faces;

	// given

	private void given_a_mesh_renderer() {
		faceRenderer = mock(FaceRenderer.class);

		meshRenderer = new MeshRenderer(faceRenderer);
	}

	private void given_a_color_selector() {
		colorSelector = mock(ColorSelector.class);
	}

	private void given_a_mesh(int numberOfFaces) {
		mesh = mock(Mesh.class);

		faces = new ArrayList<>();

		for (int i = 0; i < numberOfFaces; i++) {
			faces.add(mock(Face.class));
		}

		when(mesh.getFaces()).thenReturn(faces);
	}

	// verification

	private void verify_reset_of_color_selector() {
		verify(colorSelector, times(1)).reset();
	}

	private void verify_rendering_of_faces(int numberOfFaces) {
		assertThat(faces, hasSize(numberOfFaces));

		for (Face face : faces) {
			verify(faceRenderer, times(1)).render(face, colorSelector);
		}
	}

	private void verify_rendering_of_no_faces() {
		assertThat(faces, hasSize(0));

		verify(faceRenderer, never()).render(any(), eq(colorSelector));
	}

}