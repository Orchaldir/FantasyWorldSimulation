package jfws.util.map;

import lombok.Getter;

@Getter
public class ArrayMap2d<T> implements Map2d<T> {

	private final int width;
	private final int height;
	private final int size;
	private final T[] cells;

	public ArrayMap2d(int width, int height, T[] cells) {
		this.width = width;
		this.height = height;
		this.size = width * height;
		this.cells = cells;
	}

	@Override
	public int getIndex(int x, int y) {
		return y * width + x;
	}

	@Override
	public int getX(int index) {
		return index % width;
	}

	@Override
	public int getY(int index) {
		return index / width;
	}

	@Override
	public boolean isInsideForX(int x) {
		return x >= 0 && x < width;
	}

	@Override
	public boolean isInsideForY(int y) {
		return y >= 0 && y < height;
	}

	@Override
	public boolean isInside(int x, int y) {
		return isInsideForX(x) && isInsideForY(y);
	}

	@Override
	public boolean isInside(int index) {
		return index >=0 && index < size;
	}

	@Override
	public T getCell(int index) throws OutsideMapException {
		if(isInside(index)) {
			return cells[index];
		}

		throw new OutsideMapException(this, getX(index), getY(index), index, true);
	}

	@Override
	public T getCell(int x, int y) throws OutsideMapException {
		if(isInside(x, y)) {
			return cells[getIndex(x, y)];
		}

		throw new OutsideMapException(this, x, y, getIndex(x, y), false);
	}
}
