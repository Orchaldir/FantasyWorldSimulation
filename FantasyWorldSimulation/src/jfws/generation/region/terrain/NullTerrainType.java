package jfws.generation.region.terrain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javafx.scene.paint.Color;
import lombok.ToString;

// Uses Null Object Pattern
@AllArgsConstructor
@Getter
@ToString(of = {"name"})
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
