package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddGeneratorStep implements WorldGenerationStep {

	private final Generator generator;
	private final int index;

	@Override
	public void generate(Mesh<Void, Void, WorldCell> mesh) {
		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			generateFace(face);
		}
	}

	public void generateFace(Face<Void, Void, WorldCell> face) {
		Point2d point = Point2d.calculateCentroid(face.getPointsInCCW());
		WorldCell cell = face.getData();

		cell.addToAttribute(index, generator.generate(point));
	}
}
