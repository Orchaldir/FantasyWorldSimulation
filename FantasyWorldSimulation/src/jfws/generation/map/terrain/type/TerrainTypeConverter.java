package jfws.generation.map.terrain.type;

import java.util.Optional;

public interface TerrainTypeConverter {

	String write(TerrainType type);

	Optional<TerrainType> load(String text);
}
