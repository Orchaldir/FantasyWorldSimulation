package jfws.maps.sketch.terrain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javafx.scene.paint.Color;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString(of = {"name"})
public class TerrainTypeImpl implements TerrainType {

	private final String name;
	private final String group;
	private final Color color;
	private final double baseElevation;
	private final double elevationVariation;
	private final Double[] noiseAmplitudes;

	public boolean isDefault() {
		return false;
	}

	@Override
	public double getNoiseAmplitude(int index) {
		return noiseAmplitudes[index];
	}
}
