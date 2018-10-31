package jfws.util.map;

import java.util.Collection;

public interface Map2d<T> {

	int getWidth();
	int getHeight();
	int getSize();

	int getIndex(int x, int y);
	int getX(int index);
	int getY(int index);

	boolean isInsideForX(int x);
	boolean isInsideForY(int y);
	boolean isInside(int x, int y);
	boolean isInside(int index);

	T getCell(int index) throws OutsideMapException;
	T getCell(int x, int y) throws OutsideMapException;

	Collection<T> getCells();
}
