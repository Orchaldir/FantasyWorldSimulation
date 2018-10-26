package jfws.util.map;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToCellMapper<T> {

	private final Map2d<T> map;
	private final double originX, originY;
	private final double resolutionX, resolutionY;

	public ToCellMapper(Map2d<T> map, double originX, double originY, double resolution) {
		this(map, originX, originY, resolution, resolution);
	}

	public ToCellMapper(Map2d<T> map, double resolutionX, double resolutionY) {
		this(map, 0, 0, resolutionX, resolutionY);
	}

	public ToCellMapper(Map2d<T> map, double resolution) {
		this(map, resolution, resolution);
	}

	// coordinates to cell

	public int getCellX(double x) {
		return (int) Math.floor((x - originX) / resolutionX);
	}

	public int getCellY(double y) {
		return (int) Math.floor((y - originY) / resolutionY);
	}

	public T getCell(double x, double y) throws OutsideMapException {
		return map.getCell(getCellX(x), getCellY(y));
	}

	public double getWidth() {
		return map.getWidth() * resolutionX;
	}

	public double getHeight() {
		return map.getHeight() * resolutionY;
	}

	// cell to coordinates

	public double getCellOriginX(int x) {
		return originX + resolutionX * x;
	}

	public double getCellOriginY(int y) {
		return originY + resolutionY * y;
	}
}
