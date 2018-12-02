package jfws.features.elevation;

import jfws.util.map.CellMap2d;
import jfws.util.map.OutsideMapException;
import jfws.util.math.interpolation.Interpolator2d;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

class ElevationCellInterpolatorTest {

	public static final double ELEVATION = 99.0;
	public static final double NEW_ELEVATION = 42.0;
	public static final int X = 1;
	public static final int Y = 2;

	private Interpolator2d interpolator;
	private ElevationCellInterpolator elevationInterpolator;
	private ElevationCell cell;
	private CellMap2d<ElevationCell> cellMap;

	@BeforeEach
	public void setUp() {
		interpolator = mock(Interpolator2d.class);
		elevationInterpolator = new ElevationCellInterpolator(interpolator);
		cell = mock(ElevationCell.class);
		cellMap = mock(CellMap2d.class);
	}

	@Test
	public void testGetSourceValue() {
		when(cell.getElevation()).thenReturn(ELEVATION);

		assertThat(elevationInterpolator.getSourceValue(cell), is(equalTo(ELEVATION)));

		verify(interpolator, never()).interpolate(ArgumentMatchers.any(), anyDouble(), anyDouble());
		verify(cell).getElevation();
		verify(cell, never()).setElevation(anyDouble());
	}

	@Test
	public void testSetSourceValue() throws OutsideMapException {
		when(cellMap.getCell(X, Y)).thenReturn(cell);

		elevationInterpolator.setTargetValue(cellMap, X, Y, NEW_ELEVATION);

		verify(interpolator, never()).interpolate(ArgumentMatchers.any(), anyDouble(), anyDouble());
		verify(cell, never()).getElevation();
		verify(cell).setElevation(NEW_ELEVATION);
		verify(cellMap).getCell(X, Y);
	}

}