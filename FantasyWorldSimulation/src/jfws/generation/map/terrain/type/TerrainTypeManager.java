package jfws.generation.map.terrain.type;

import jfws.util.io.FileUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

	public List<String> getNames() {
		return new ArrayList<>(terrainTypeMap.keySet());
	}

	public int size() {
		return terrainTypeMap.size();
	}

	public void load(File file) {
		try {
			String text = fileUtils.readWholeFile(file);
			List<TerrainType> terrainTypes = converter.load(text);

			log.info("load(): file={} N={}", file.getPath(), terrainTypes.size());

			for(TerrainType terrainType : terrainTypes) {
				add(terrainType);
			}
		} catch (IOException e) {
			log.warn("load(): IOException: {}", e.getMessage());
		}
	}

	public void save(File file) throws IOException {
		try {
			log.info("save(): file={}", file.getPath());

			String text = converter.save(terrainTypeMap.values());

			fileUtils.writeWholeFile(file, text);
		} catch (IOException e) {
			log.warn("save(): IOException: {}", e.getMessage());
			throw e;
		}
	}
}
