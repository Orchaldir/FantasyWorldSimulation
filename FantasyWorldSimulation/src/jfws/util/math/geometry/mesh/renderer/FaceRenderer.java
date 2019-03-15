package jfws.util.math.geometry.mesh.renderer;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@AllArgsConstructor
@Slf4j
public class FaceRenderer<V,E,F> {

	private final Renderer renderer;

	public void render(Face<V,E,F> face, ColorSelector<F> colorSelector) {
		List<Point2d> polygonPoints = face.getPointsInCCW();

		renderer.setColor(colorSelector.select(face.getData()));
		renderer.renderPolygon(polygonPoints);
	}
}
