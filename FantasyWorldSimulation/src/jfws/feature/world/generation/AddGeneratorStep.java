package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.map.CellMap2d;
import jfws.util.map.ToCellMapper;
import jfws.util.math.generator.Generator;
import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.mesh.Face;
import jfws.util.math.geometry.mesh.Mesh;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AddGeneratorStep implements WorldGenerationStep {

	private final Generator generator;
	private final int attributeIndex;

	@Override
	public void generate(ToCellMapper<WorldCell> mapper) {
		CellMap2d<WorldCell> map = mapper.getMap();

		for(int x = 0; x < map.getWidth(); x++) {
			for(int y = 0; y < map.getHeight(); y++) {
				Point2d point = mapper.getCellOrigin(x, y);
				generateCell(map.getCell(x, y), point);
			}
		}
	}

	@Override
	public void generate(Mesh<Void, Void, WorldCell> mesh) {
		for (Face<Void, Void, WorldCell> face : mesh.getFaces()) {
			Point2d point = Point2d.calculateCentroid(face.getPointsInCCW());
			generateCell(face.getData(), point);
		}
	}

	public void generateCell(WorldCell cell, Point2d point) {
		cell.addToAttribute(attributeIndex, generator.generate(point));
	}
}
