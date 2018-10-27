package jfws.generation.region.terrain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javafx.scene.paint.Color;

@AllArgsConstructor
@Getter
public class TerrainTypeImpl implements TerrainType {

	private final String name;
	private final Color color;

	public boolean isDefault() {
		return false;
	}
}
