package jfws.util.math.geometry.voronoi;

import lombok.Getter;

@Getter
class ClosestPointData {
	private int closestPointId = -1;
	private double distance = Double.MAX_VALUE;

	public void update(int closestPointId, double distance) {
		this.closestPointId = closestPointId;
		this.distance = distance;
	}
}
