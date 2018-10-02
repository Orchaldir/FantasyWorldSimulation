package jfws.util.map;

public interface IMap2d {

	int getWidth();

	int getHeight();

	int getSize();

	int getIndex(int x, int y);

	int getX(int index);

	int getY(int index);
}
