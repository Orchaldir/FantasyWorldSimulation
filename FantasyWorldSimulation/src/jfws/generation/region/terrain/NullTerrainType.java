package jfws.generation.region.terrain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javafx.scene.paint.Color;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

// Uses Null Object Pattern
@Getter
@Slf4j
@ToString(of = {"name"})
public class NullTerrainType implements TerrainType {

	public static final Color DEFAULT_COLOR = Color.PINK;

	private final String name;

	public NullTerrainType(String name) {
		this.name = name;
		log.warn("NullTerrainType(): name='{}'", name);
	}

	@Override
	public Color getColor() {
		return DEFAULT_COLOR;
	}

	public boolean isDefault() {
		return true;
	}
}
