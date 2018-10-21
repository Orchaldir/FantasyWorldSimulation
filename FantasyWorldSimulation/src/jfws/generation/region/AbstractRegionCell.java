package jfws.generation.region;

import jfws.generation.region.terrain.TerrainType;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AbstractRegionCell {

	private TerrainType terrainType;
}
