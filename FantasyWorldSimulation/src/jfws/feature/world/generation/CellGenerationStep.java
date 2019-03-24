package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;

public abstract class CellGenerationStep implements WorldGenerationStep {
	@Override
	public void generate(Mesh<Void, Void, WorldCell> mesh) {
		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			generateCell(face);
		}
	}

	public abstract void generateCell(Face<Void, Void, WorldCell> face);
}
