package jfws.generation.map;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@AllArgsConstructor
@Getter
public class TerrainType {

	public static final String DEFAULT_NAME = "DEFAULT";
	public static final TerrainType DEFAULT_TYPE = new TerrainType(DEFAULT_NAME, Color.PINK);

	private final String name;
	private final Color color;

	public boolean isDefault() {
		return this == DEFAULT_TYPE;
	}
}
