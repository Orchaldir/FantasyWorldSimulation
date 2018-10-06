package jfws.util.map;

public class SharedTestData {

	public static final int WIDTH = 4;
	public static final int HEIGHT = 5;
	public static final int SIZE = WIDTH * HEIGHT;

	public static final int X0 = 0, Y0 = 0, INDEX0 = 0;
	public static final int X1 = 2, Y1 = 0, INDEX1 = 2;
	public static final int X2 = 0, Y2 = 3, INDEX2 = 12;
	public static final int X3 = 2, Y3 = 1, INDEX3 = 6;

	public static final IMap2d<Integer> MAP;

	static
	{
		Integer[] array = new Integer[SIZE];

		for(int i = 0; i < SIZE; i++) {
			array[i] = i;
		}

		MAP = new ArrayMap2d<>(WIDTH, HEIGHT, array);
	}
}
