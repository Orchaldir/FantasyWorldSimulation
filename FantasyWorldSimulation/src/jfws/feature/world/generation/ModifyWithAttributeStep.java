package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
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
	public void generate(ToCellMapper<WorldCell> mapper) {
		CellMap2d<WorldCell> map = mapper.getMap();

		for(int x = 0; x < map.getWidth(); x++) {
			for(int y = 0; y < map.getHeight(); y++) {
				generateCell(map.getCell(x, y));
			}
		}
	}

	@Override
	public void generate(Mesh<Void, Void, WorldCell> mesh) {
		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			generateCell(face.getData());
		}
	}

	public void generateCell(WorldCell cell) {
		double value = factor * Math.min(Math.max(cell.getAttribute(sourceIndex), minValue), maxValue);
		cell.addToAttribute(targetIndex, value);
	}
}
