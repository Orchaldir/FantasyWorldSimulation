package jfws.maps.region;

import jfws.features.elevation.ElevationCell;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RegionCell implements ElevationCell {

	private double elevation;

}
