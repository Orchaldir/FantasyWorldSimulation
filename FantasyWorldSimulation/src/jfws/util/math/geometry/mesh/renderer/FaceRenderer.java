package jfws.util.math.geometry.mesh.renderer;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.rendering.ColorSelector;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Slf4j
public class FaceRenderer {

	private final Renderer renderer;

	public void render(Face face, ColorSelector<Face> colorSelector) {
		List<Point2d> polygonPoints = face.getPointsInCCW();

		log.info("render(): face={} pointss={}", face.getId(), polygonPoints.size());

		renderer.setColor(colorSelector.select(face));
		renderer.renderPolygon(polygonPoints);

		log.info("render(): finished");
	}
}
