package jfws.feature.world.generation;

import jfws.feature.world.WorldCell;
import jfws.util.math.geometry.mesh.Face;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ModifyWithAttributeStep extends CellGenerationStep {

	private final int sourceIndex;
	private final int targetIndex;
	private final double minValue;
	private final double maxValue;
	private final double factor;

	@Override
	public void generateCell(Face<Void, Void, WorldCell> face) {
		WorldCell cell = face.getData();

		double value = factor * Math.min(Math.max(cell.getAttribute(sourceIndex), minValue), maxValue);
		cell.addToAttribute(targetIndex, value);
	}
}
