package jfws.util.math.geometry.mesh.renderer;

import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import jfws.util.rendering.ColorSelector;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class MeshRenderer {

	private final FaceRenderer faceRenderer;

	public void renderFaces(Mesh mesh, ColorSelector<Face> colorSelector) {
		List<Face> faces = mesh.getFaces();

		log.info("renderFaces(): faces={}", faces.size());

		colorSelector.reset();

		for (Face face : faces) {
			faceRenderer.render(face, colorSelector);
		}
	}
}
