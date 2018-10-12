package jfws.util.map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
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

	public int getCellX(int x) {
		return (int) Math.floor((x - originX) / (double)resolutionX);
	}

	public int getCellY(int y) {
		return (int) Math.floor((y - originY) / (double)resolutionY);
	}

	public T getCell(int x, int y) throws OutsideMapException {
		return map.getCell(getCellX(x), getCellY(y));
	}

	public double getWidth() {
		return map.getWidth() * resolutionX;
	}

	public double getHeight() {
		return map.getHeight() * resolutionY;
	}
}
