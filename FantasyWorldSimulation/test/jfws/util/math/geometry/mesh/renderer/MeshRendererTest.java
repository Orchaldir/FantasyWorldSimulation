package jfws.util.math.geometry.mesh.renderer;

import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.MeshBuilder;
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
		createMeshRenderer();
		createColorSelector();
		createMesh(3);

		meshRenderer.renderFaces(mesh, colorSelector);

		verifyResetOfColorSelector();
		verifyRenderingOfFaces(3);
	}

	@Test
	public void testRenderNoFaces() {
		createMeshRenderer();
		createColorSelector();
		createMesh(0);

		meshRenderer.renderFaces(mesh, colorSelector);

		verifyResetOfColorSelector();
		verifyRenderingOfNoFaces();
	}

	//

	private FaceRenderer faceRenderer;
	private MeshRenderer meshRenderer;
	private ColorSelector colorSelector;
	private MeshBuilder mesh;
	private List<Face> faces;

	// given

	private void createMeshRenderer() {
		faceRenderer = mock(FaceRenderer.class);

		meshRenderer = new MeshRenderer(faceRenderer);
	}

	private void createColorSelector() {
		colorSelector = mock(ColorSelector.class);
	}

	private void createMesh(int numberOfFaces) {
		mesh = mock(MeshBuilder.class);

		faces = new ArrayList<>();

		for (int i = 0; i < numberOfFaces; i++) {
			faces.add(mock(Face.class));
		}

		when(mesh.getFaces()).thenReturn(faces);
	}

	// verification

	private void verifyResetOfColorSelector() {
		verify(colorSelector, times(1)).reset();
	}

	private void verifyRenderingOfFaces(int numberOfFaces) {
		assertThat(faces, hasSize(numberOfFaces));

		for (Face face : faces) {
			verify(faceRenderer, times(1)).render(face, colorSelector);
		}
	}

	private void verifyRenderingOfNoFaces() {
		assertThat(faces, hasSize(0));

		verify(faceRenderer, never()).render(any(), eq(colorSelector));
	}

}