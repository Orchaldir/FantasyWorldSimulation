package jfws.maps.sketch.terrain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javafx.scene.paint.Color;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString(of = {"name"})
public class TerrainTypeImpl implements TerrainType {

	private final String name;
	private final String group;
	private final Color color;
	private final double baseElevation;
	private final double elevationVariation;

	public TerrainTypeImpl(String name, Color color, double baseElevation, double elevationVariation) {
		this(name, "", color, baseElevation, elevationVariation);
	}

	public boolean isDefault() {
		return false;
	}
}
