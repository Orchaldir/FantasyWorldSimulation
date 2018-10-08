package jfws.generation.map.terrain.type;

import jfws.util.io.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Slf4j
public class TerrainTypeManager {

	private final FileUtils fileUtils;
	private final TerrainTypeConverter converter;
	private final Map<String, TerrainType> terrainTypeMap = new HashMap<>();

	public void add(TerrainType terrainType) {
		terrainTypeMap.put(terrainType.getName(), terrainType);
	}

	public TerrainType getOrDefault(String name) {
		return terrainTypeMap.computeIfAbsent(name, NullTerrainType::new);
	}

	public int size() {
		return terrainTypeMap.size();
	}

	public void load(File file) {
		try {
			String json = fileUtils.readWholeFile(file);

			for(TerrainType terrainType : converter.load(json)) {
				add(terrainType);
			}
		} catch (IOException e) {
			log.warn("load(): IOException: {}", e.getMessage());
		}
	}
}
