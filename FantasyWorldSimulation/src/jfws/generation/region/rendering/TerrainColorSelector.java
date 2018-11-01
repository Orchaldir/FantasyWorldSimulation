package jfws.generation.region.rendering;

import javafx.scene.paint.Color;
import jfws.generation.region.AbstractRegionCell;
import jfws.util.rendering.ColorSelector;

public class TerrainColorSelector implements ColorSelector<AbstractRegionCell> {

	public static final String NAME = "Terrain";

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public Color select(AbstractRegionCell cell) {
		return cell.getTerrainType().getColor();
	}
}
