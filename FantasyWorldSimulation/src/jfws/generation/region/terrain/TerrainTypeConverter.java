package jfws.generation.region.terrain;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TerrainTypeConverter {

	String save(Collection<TerrainType> types);
	String saveTerrainType(TerrainType type);

	List<TerrainType> load(String text);
	Optional<TerrainType> loadTerrainType(String text);
}
