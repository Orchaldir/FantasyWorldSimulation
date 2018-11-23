package jfws.maps.sketch.terrain;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface TerrainTypeConverter {

	String save(Collection<TerrainType> types);
	String saveTerrainType(TerrainType type);

	Map<String,TerrainType> load(String text) throws IOException;
	Optional<TerrainType> loadTerrainType(String text);
}
