package jfws.generation.map.terrain.type;

public interface TerrainTypeConverter {

	String write(TerrainType type);

	TerrainType load(String text);
}
