package jfws.generation.map.terrain.type;

import java.util.HashMap;
import java.util.Map;

public class TerrainTypeManager {

	private Map<String, TerrainType> terrainTypeMap = new HashMap<>();

	public void add(TerrainType terrainType) {
		terrainTypeMap.put(terrainType.getName(), terrainType);
	}

	public TerrainType getOrDefault(String name) {
		return terrainTypeMap.computeIfAbsent(name, NullTerrainType::new);
	}
}
