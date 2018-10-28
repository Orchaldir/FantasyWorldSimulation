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
	public static final double DEFAULT_BASE_ELEVATION = 0;
	public static final double DEFAULT_ELEVATION_VARIATION = 0;

	private final String name;

	public NullTerrainType(String name) {
		this.name = name;
		log.warn("NullTerrainType(): name='{}'", name);
	}

	@Override
	public Color getColor() {
		return DEFAULT_COLOR;
	}

	@Override
	public double getBaseElevation() {
		return DEFAULT_BASE_ELEVATION;
	}

	@Override
	public double getElevationVariation() {
		return DEFAULT_ELEVATION_VARIATION;
	}

	public boolean isDefault() {
		return true;
	}
}
