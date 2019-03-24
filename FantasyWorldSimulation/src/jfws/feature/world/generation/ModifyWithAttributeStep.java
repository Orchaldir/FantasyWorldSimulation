package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ModifyWithAttributeStep implements WorldGenerationStep {

	private final int sourceIndex;
	private final int targetIndex;
	private final double minValue;
	private final double maxValue;
	private final double factor;

	@Override
	public void generate(Mesh<Void, Void, WorldCell> mesh) {
		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			generateFace(face);
		}
	}

	public void generateFace(Face<Void, Void, WorldCell> face) {
		WorldCell cell = face.getData();

		double value = factor * Math.min(Math.max(cell.getAttribute(sourceIndex), minValue), maxValue);
		cell.addToAttribute(targetIndex, value);
	}
}
