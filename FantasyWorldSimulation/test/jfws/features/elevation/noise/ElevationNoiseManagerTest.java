package jfws.features.elevation.noise;

import jfws.maps.region.RegionCell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ElevationNoiseManagerTest {

	public static final String NAME_0 = "name0";
	public static final String NAME_1 = "name1";
	public static final String NAME_2 = "name2";

	private ElevationNoise noise0;
	private ElevationNoise noise1;
	private ElevationNoise noise2;

	private ElevationNoiseManager<RegionCell> manager;

	@BeforeEach
	public void setUp() {
		noise0 = mock(ElevationNoise.class);
		noise1 = mock(ElevationNoise.class);
		noise2 = mock(ElevationNoise.class);

		manager = new ElevationNoiseManager();
	}

	// add()

	@Test
	public void testAdd() {
		when(noise0.getName()).thenReturn(NAME_0);

		manager.add(noise0);

		verify(noise0, atLeastOnce()).getName();
	}

	@Test
	public void testAddTwo() {
		when(noise0.getName()).thenReturn(NAME_0);
		when(noise1.getName()).thenReturn(NAME_1);

		manager.add(noise0);
		manager.add(noise1);

		verify(noise0, atLeastOnce()).getName();
		verify(noise1, atLeastOnce()).getName();
	}

	@Test
	public void testAddThree() {
		when(noise0.getName()).thenReturn(NAME_0);
		when(noise1.getName()).thenReturn(NAME_1);
		when(noise2.getName()).thenReturn(NAME_2);

		manager.add(noise0);
		manager.add(noise1);
		manager.add(noise2);

		verify(noise0, atLeastOnce()).getName();
		verify(noise1, atLeastOnce()).getName();
		verify(noise2, atLeastOnce()).getName();
	}

	@Test
	public void testAddTwice() {
		when(noise0.getName()).thenReturn(NAME_0);

		manager.add(noise0);

		assertThrows(IllegalArgumentException.class, () -> manager.add(noise0));
	}

	// getAll()

	@Test
	public void testGetAllEmpty() {
		assertThat(manager.getAll(), hasSize(0));
	}

	@Test
	public void testGetAll() {
		when(noise0.getName()).thenReturn(NAME_0);
		when(noise1.getName()).thenReturn(NAME_1);
		when(noise2.getName()).thenReturn(NAME_2);

		manager.add(noise0);
		manager.add(noise1);
		manager.add(noise2);

		assertThat(manager.getAll(), containsInAnyOrder(noise0, noise1, noise2));
	}

	// getSize()

	@Test
	public void testGetSizeOfEmpty() {
		assertThat(manager.getSize(), is(0));
	}

	@Test
	public void testGetSize() {
		when(noise0.getName()).thenReturn(NAME_0);

		manager.add(noise0);

		assertThat(manager.getSize(), is(1));
	}

	@Test
	public void testGetSizeTwo() {
		when(noise0.getName()).thenReturn(NAME_0);
		when(noise1.getName()).thenReturn(NAME_1);

		manager.add(noise0);
		manager.add(noise1);

		assertThat(manager.getSize(), is(2));
	}

	@Test
	public void testGetSizeThree() {
		when(noise0.getName()).thenReturn(NAME_0);
		when(noise1.getName()).thenReturn(NAME_1);
		when(noise2.getName()).thenReturn(NAME_2);

		manager.add(noise0);
		manager.add(noise1);
		manager.add(noise2);

		assertThat(manager.getSize(), is(3));
	}

}