package jfws.app.demo.biome;

import jfws.features.elevation.ElevationCell;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Cell implements ElevationCell {

	private double elevation;
}
