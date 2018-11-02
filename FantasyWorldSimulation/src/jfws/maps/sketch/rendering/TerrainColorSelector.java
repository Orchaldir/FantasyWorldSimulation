package jfws.maps.sketch.rendering;

import javafx.scene.paint.Color;
import jfws.maps.sketch.SketchCell;
import jfws.util.rendering.ColorSelector;

public class TerrainColorSelector implements ColorSelector<SketchCell> {

	public static final String NAME = "Terrain";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Color select(SketchCell cell) {
		return cell.getTerrainType().getColor();
	}
}
