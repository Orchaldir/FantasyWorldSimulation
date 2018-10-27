package jfws.generation.region;

import jfws.generation.region.terrain.TerrainType;
import jfws.util.rendering.ColorSelector;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AbstractRegionCell {

	public final static ColorSelector<AbstractRegionCell> TERRAIN_COLOR_SELECTOR = cell -> cell.getTerrainType().getColor();

	private TerrainType terrainType;
}
