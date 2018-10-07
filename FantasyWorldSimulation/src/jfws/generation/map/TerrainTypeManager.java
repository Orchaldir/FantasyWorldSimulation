package jfws.generation.map;

import java.util.HashMap;
import java.util.Map;

public class TerrainTypeManager {

	private Map<String, TerrainType> terrainTypeMap = new HashMap<>();

	public TerrainTypeManager() {
		add(TerrainType.DEFAULT_TYPE);
	}

	public void add(TerrainType terrainType) {
		terrainTypeMap.put(terrainType.getName(), terrainType);
	}

	public TerrainType getOrDefault(String name) {
		return terrainTypeMap.getOrDefault(name, TerrainType.DEFAULT_TYPE);
	}
}
