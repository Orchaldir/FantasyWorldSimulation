package jfws.util.map;

import lombok.Getter;

@Getter
public class ArrayMap2d implements IMap2d {

	private final int width;
	private final int height;
	private final int size;

	public ArrayMap2d(int width, int height) {
		this.width = width;
		this.height = height;
		this.size = width * height;
	}

	@Override
	public int getIndex(int x, int y) {
		return y * width + x;
	}
}
