package jfws.util.map;

import jfws.util.math.geometry.Point2d;
import jfws.util.math.geometry.Rectangle;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ToCellMapper<T> {

	private final CellMap2d<T> map;
	private final double originX;
	private final double originY;
	private final double resolutionX;
	private final double resolutionY;

	public ToCellMapper(CellMap2d<T> map, double originX, double originY, double resolution) {
		this(map, originX, originY, resolution, resolution);
	}

	public ToCellMapper(CellMap2d<T> map, double resolutionX, double resolutionY) {
		this(map, 0, 0, resolutionX, resolutionY);
	}

	public ToCellMapper(CellMap2d<T> map, double resolution) {
		this(map, resolution, resolution);
	}

	public static <T> ToCellMapper fromRectangle(CellMap2d<T> map, Rectangle rectangle) {
		double resolutionX = rectangle.getSize().getX() / map.getWidth();
		double resolutionY = rectangle.getSize().getY() / map.getHeight();

		return new ToCellMapper(map, rectangle.getStart().getX(), rectangle.getStart().getY(),
				resolutionX, resolutionY);
	}

	// coordinates to cell

	public int getCellX(double x) {
		return (int) Math.floor((x - originX) / resolutionX);
	}

	public int getCellY(double y) {
		return (int) Math.floor((y - originY) / resolutionY);
	}

	public int getIndex(double x, double y) {
		return map.getIndex(getCellX(x), getCellY(y));
	}

	public T getCell(double x, double y) {
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

	public Point2d getCellOrigin(int x, int y) {
		return new Point2d(getCellOriginX(x), getCellOriginY(y));
	}
}
