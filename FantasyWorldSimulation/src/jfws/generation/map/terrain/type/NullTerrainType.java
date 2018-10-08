package jfws.generation.map.terrain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

// Uses Null Object Pattern
@AllArgsConstructor
@Getter
public class NullTerrainType implements TerrainType {

	public static final Color DEFAULT_COLOR = Color.PINK;

	private final String name;

	@Override
	public Color getColor() {
		return DEFAULT_COLOR;
	}

	public boolean isDefault() {
		return true;
	}
}
