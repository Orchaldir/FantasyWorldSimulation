package jfws.generation.map.terrain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@AllArgsConstructor
@Getter
public class TerrainTypeImpl implements TerrainType {

	private final String name;
	private final Color color;

	public boolean isDefault() {
		return false;
	}
}
