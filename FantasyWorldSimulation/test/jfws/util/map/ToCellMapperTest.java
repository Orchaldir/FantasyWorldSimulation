package jfws.util.map;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ToCellMapperTest extends SharedTestData {

	public static final double ERROR = 0.001;

	public static final int ORIGIN_X = 3;
	public static final int ORIGIN_Y = 4;
	public static final int RESOLUTION_X = 2;
	public static final int RESOLUTION_Y = 3;

	public static final int MAX_X = ORIGIN_X + WIDTH * RESOLUTION_X - 1;
	public static final int MAX_Y = ORIGIN_Y + HEIGHT * RESOLUTION_Y - 1;

	public static final int CELL_X_0 = ORIGIN_X + 1 * RESOLUTION_X / 2;
	public static final int CELL_X_1 = ORIGIN_X + 3 * RESOLUTION_X / 2;
	public static final int CELL_X_2 = ORIGIN_X + 5 * RESOLUTION_X / 2;

	public static final int CELL_Y_0 = ORIGIN_Y + 1 * RESOLUTION_Y / 2;
	public static final int CELL_Y_1 = ORIGIN_Y + 3 * RESOLUTION_Y / 2;
	public static final int CELL_Y_2 = ORIGIN_Y + 5 * RESOLUTION_Y / 2;

	private static final ToCellMapper<Integer> MAPPER = new ToCellMapper<>(MAP, ORIGIN_X, ORIGIN_Y, RESOLUTION_X, RESOLUTION_Y);

	// getCellX()

	@Test
	void testGetCellX() {
		assertThat(MAPPER.getCellX(CELL_X_0), is(equalTo(0)));
		assertThat(MAPPER.getCellX(CELL_X_1), is(equalTo(1)));
		assertThat(MAPPER.getCellX(CELL_X_2), is(equalTo(2)));
	}

	@Test
	void testGetCellX_MinBorder() {
		assertThat(MAPPER.getCellX(ORIGIN_X - 1), is(equalTo(-1)));
		assertThat(MAPPER.getCellX(ORIGIN_X), is(equalTo(0)));
	}

	@Test
	void testGetCellX_MaxBorder() {
		assertThat(MAPPER.getCellX(MAX_X), is(equalTo(WIDTH - 1)));
		assertThat(MAPPER.getCellX(MAX_X +  1), is(equalTo(WIDTH)));
	}

	// getCellY()

	@Test
	void testGetCellY() {
		assertThat(MAPPER.getCellY(CELL_Y_0), is(equalTo(0)));
		assertThat(MAPPER.getCellY(CELL_Y_1), is(equalTo(1)));
		assertThat(MAPPER.getCellY(CELL_Y_2), is(equalTo(2)));
	}

	@Test
	void testGetCellY_MinBorder() {
		assertThat(MAPPER.getCellY(ORIGIN_Y - 1), is(equalTo(-1)));
		assertThat(MAPPER.getCellY(ORIGIN_Y), is(equalTo(0)));
	}

	@Test
	void testGetCellY_MaxBorder() {
		assertThat(MAPPER.getCellY(MAX_Y), is(equalTo(HEIGHT - 1)));
		assertThat(MAPPER.getCellY(MAX_Y +  1), is(equalTo(HEIGHT)));
	}

	// getCell()

	private void testGetCell(int cell_x, int cell_y) throws OutsideMapException {
		int index  = MAP.getIndex(cell_x, cell_y);

		for(int x = ORIGIN_X  + RESOLUTION_X * cell_x; x < ORIGIN_X + RESOLUTION_X * (cell_x + 1); x++) {

			for(int y = ORIGIN_Y + RESOLUTION_Y * cell_y; y < ORIGIN_Y + RESOLUTION_Y * (cell_y + 1); y++) {
				assertThat(MAPPER.getCell(x, y), is(equalTo(index)));
			}
		}
	}

	@Test()
	void testGetCell() throws OutsideMapException {
		for(int cell_y = 0; cell_y < HEIGHT; cell_y++) {
			for(int cell_x = 0; cell_x < WIDTH; cell_x++) {
				testGetCell(cell_x, cell_y);
			}
		}
	}

	@Test()
	void testGetCellOutside(){
		OutsideMapException exception = assertThrows(OutsideMapException.class, () -> MAPPER.getCell(ORIGIN_X - 1, ORIGIN_Y - 1));

		assertThat(exception.getMap(), is(equalTo(MAP)));
		assertThat(exception.getX(), is(equalTo(-1)));
		assertThat(exception.getY(), is(equalTo(-1)));
	}

	// getWidth()

	@Test()
	void testGetWidth(){
		assertThat(MAPPER.getWidth(), is(closeTo(WIDTH * RESOLUTION_X, ERROR)));
	}

	// getHeight()

	@Test()
	void testGetHeight(){
		assertThat(MAPPER.getHeight(), is(closeTo(HEIGHT * RESOLUTION_Y, ERROR)));
	}

	// getCellOriginX()

	@Test()
	void testGetCellOriginX(){
		assertThat(MAPPER.getCellOriginX(0), is(closeTo(ORIGIN_X, ERROR)));
		assertThat(MAPPER.getCellOriginX(1), is(closeTo(ORIGIN_X + RESOLUTION_X, ERROR)));
		assertThat(MAPPER.getCellOriginX(2), is(closeTo(ORIGIN_X + RESOLUTION_X * 2, ERROR)));
		assertThat(MAPPER.getCellOriginX(3), is(closeTo(ORIGIN_X + RESOLUTION_X * 3, ERROR)));
	}

	// getCellOriginY()

	@Test()
	void testGetCellOriginY(){
		assertThat(MAPPER.getCellOriginY(0), is(closeTo(ORIGIN_Y, ERROR)));
		assertThat(MAPPER.getCellOriginY(1), is(closeTo(ORIGIN_Y + RESOLUTION_Y, ERROR)));
		assertThat(MAPPER.getCellOriginY(2), is(closeTo(ORIGIN_Y + RESOLUTION_Y * 2, ERROR)));
		assertThat(MAPPER.getCellOriginY(3), is(closeTo(ORIGIN_Y + RESOLUTION_Y * 3, ERROR)));
	}

}