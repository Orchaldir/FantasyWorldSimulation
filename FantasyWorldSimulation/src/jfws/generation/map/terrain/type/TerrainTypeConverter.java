package jfws.generation.map.terrain.type;

import java.util.List;
import java.util.Optional;

public interface TerrainTypeConverter {

	String save(List<TerrainType> types);
	String saveTerrainType(TerrainType type);

	List<TerrainType> load(String text);
	Optional<TerrainType> loadTerrainType(String text);
}
