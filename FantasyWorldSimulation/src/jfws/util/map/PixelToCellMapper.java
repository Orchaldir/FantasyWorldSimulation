package jfws.util.map;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PixelToCellMapper<T> {

	private final Map2d<T> map;
	private final int originX, originY;
	private final int resolutionX, resolutionY;

	public PixelToCellMapper(Map2d<T> map, int originX, int originY, int resolution) {
		this(map, originX, originY, resolution, resolution);
	}

	public PixelToCellMapper(Map2d<T> map, int resolutionX, int resolutionY) {
		this(map, 0, 0, resolutionX, resolutionY);
	}

	public PixelToCellMapper(Map2d<T> map, int resolution) {
		this(map, 0, 0, resolution, resolution);
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
}
