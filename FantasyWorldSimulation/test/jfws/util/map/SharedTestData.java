package jfws.util.map;

public class SharedTestData {

	// Map2d

	public static final int WIDTH = 4;
	public static final int HEIGHT = 5;
	public static final int SIZE = WIDTH * HEIGHT;

	public static final int X0 = 0, Y0 = 0, INDEX0 = 0;
	public static final int X1 = 2, Y1 = 0, INDEX1 = 2;
	public static final int X2 = 0, Y2 = 3, INDEX2 = 12;
	public static final int X3 = 2, Y3 = 1, INDEX3 = 6;

	public static final Map2d<Integer> MAP;

	static
	{
		Integer[] array = new Integer[SIZE];

		for(int i = 0; i < SIZE; i++) {
			array[i] = i;
		}

		MAP = new ArrayMap2d<>(WIDTH, HEIGHT, array);
	}

	// ToCellMapper

	public static final double ERROR = 0.001;

	public static final int ORIGIN_X = 3;
	public static final int ORIGIN_Y = 4;
	public static final int RESOLUTION_X = 2;
	public static final int RESOLUTION_Y = 3;

	public static final int MAX_X = ORIGIN_X + WIDTH * RESOLUTION_X - 1;
	public static final int MAX_Y = ORIGIN_Y + HEIGHT * RESOLUTION_Y - 1;

	public static final int CELL_X_0 = ORIGIN_X;
	public static final int CELL_X_1 = ORIGIN_X + 1 * RESOLUTION_X;
	public static final int CELL_X_2 = ORIGIN_X + 2 * RESOLUTION_X;
	public static final int CELL_X_3 = ORIGIN_X + 3 * RESOLUTION_X;

	public static final int CELL_Y_0 = ORIGIN_Y;
	public static final int CELL_Y_1 = ORIGIN_Y + 1 * RESOLUTION_Y;
	public static final int CELL_Y_2 = ORIGIN_Y + 2 * RESOLUTION_Y;
	public static final int CELL_Y_3 = ORIGIN_Y + 3 * RESOLUTION_Y;
	public static final int CELL_Y_4 = ORIGIN_Y + 4 * RESOLUTION_Y;

	public static final int CENTER_X_0 = ORIGIN_X + 1 * RESOLUTION_X / 2;
	public static final int CENTER_X_1 = ORIGIN_X + 3 * RESOLUTION_X / 2;
	public static final int CENTER_X_2 = ORIGIN_X + 5 * RESOLUTION_X / 2;

	public static final int CENTER_Y_0 = ORIGIN_Y + 1 * RESOLUTION_Y / 2;
	public static final int CENTER_Y_1 = ORIGIN_Y + 3 * RESOLUTION_Y / 2;
	public static final int CENTER_Y_2 = ORIGIN_Y + 5 * RESOLUTION_Y / 2;

	public static final ToCellMapper<Integer> MAPPER = new ToCellMapper<>(MAP, ORIGIN_X, ORIGIN_Y, RESOLUTION_X, RESOLUTION_Y);
}
