package jfws.generation.region.terrain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javafx.scene.paint.Color;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = {"name"})
public class TerrainTypeImpl implements TerrainType {

	private final String name;
	private final Color color;
	private final double baseElevation;
	private final double elevationVariation;

	public boolean isDefault() {
		return false;
	}
}
