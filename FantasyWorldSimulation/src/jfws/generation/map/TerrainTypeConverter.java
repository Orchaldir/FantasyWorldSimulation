package jfws.generation.map;

public interface TerrainTypeConverter {

	String write(TerrainType type);

	TerrainType load(String text);
}
