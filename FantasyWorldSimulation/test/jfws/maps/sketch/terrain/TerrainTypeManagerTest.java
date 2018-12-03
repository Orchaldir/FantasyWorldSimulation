package jfws.maps.sketch.terrain;

import jfws.util.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static jfws.maps.sketch.terrain.TerrainType.NO_GROUP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class TerrainTypeManagerTest extends SharedTestData {

	public static final String MOCKED_JSON = "mocked-json";
	public static final String INVALID_JSON = "invalid-json";

	private FileUtils fileUtils;
	private TerrainTypeConverter converter;
	private TerrainTypeManager manager;

	@BeforeEach
	public void setUp() {
		fileUtils = mock(FileUtils.class);
		converter = mock(TerrainTypeConverter.class);
		manager = new TerrainTypeManager(fileUtils, converter);
	}

	// add & get

	@Test
	public void testAddAndGetTerrainTypes() {
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		assertThat(manager.size(), is(equalTo(3)));

		assertThat(manager.getOrDefault(NAME_A), is(equalTo(TERRAIN_TYPE_A)));
		assertThat(manager.getOrDefault(NAME_B), is(equalTo(TERRAIN_TYPE_B)));
		assertThat(manager.getOrDefault(NAME_C), is(equalTo(TERRAIN_TYPE_C)));
	}

	@Test
	public void testGetUnknownTerrainType() {
		TerrainType type = manager.getOrDefault(NAME_A);

		assertThat(type, is(notNullValue()));
		assertThat(type, is(instanceOf(NullTerrainType.class)));
		assertThat(type.getName(), is(equalTo(NAME_A)));
	}

	@Test
	public void testGetUnknownTerrainTypeTwice() {
		TerrainType type0 = manager.getOrDefault(NAME_A);
		TerrainType type1 = manager.getOrDefault(NAME_A);

		assertThat(manager.size(), is(equalTo(1)));

		assertThat(type0, is(notNullValue()));
		assertThat(type0, is(sameInstance(type1)));
	}

	// getNames()

	@Test
	public void testGetNamesEmpty() {
		assertTrue(manager.getNames().isEmpty());
	}

	@Test
	public void testGetNames() {
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		assertThat(manager.getNames(), containsInAnyOrder(NAME_B, NAME_C));
	}

	// getNamesForGroup()

	@Test
	public void testGetNamesForGroupEmpty() {
		assertTrue(manager.getNamesForGroup(GROUP0).isEmpty());
	}

	@Test
	public void testGetNamesForGroup() {
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		assertThat(manager.getNamesForGroup(NO_GROUP), containsInAnyOrder(NAME_A, NAME_B));
		assertThat(manager.getNamesForGroup(GROUP0), containsInAnyOrder(NAME_C));
	}

	// getNamesSortedByGroup()

	@Test
	public void testGetNameSortedByGroupEmpty() {
		assertTrue(manager.getNamesSortedByGroup().isEmpty());
	}

	@Test
	public void testGetNameSortedByGroup() {
		manager.add(TERRAIN_TYPE_D);
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_C);
		manager.add(TERRAIN_TYPE_B);

		assertThat(manager.getNamesSortedByGroup(), contains(NAME_A, NAME_B, NAME_D, NAME_C));
	}

	// getGroups()

	@Test
	public void testGetGroupsEmpty() {
		assertTrue(manager.getGroups().isEmpty());
	}

	@Test
	public void testGetNoGroups() {
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);

		assertTrue(manager.getGroups().isEmpty());
	}

	@Test
	public void testGetGroups() {
		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		assertThat(manager.getGroups(), containsInAnyOrder(GROUP0));
	}

	// load()

	@Test
	public void testLoad() throws IOException {
		File file = new File("test.json");

		when(fileUtils.readWholeFile(file)).thenReturn(MOCKED_JSON);
		when(converter.load(MOCKED_JSON)).thenReturn(ALL_TYPES);

		manager.load(file);

		assertThat(manager.size(), is(equalTo(3)));

		assertThat(manager.getOrDefault(NAME_A), is(equalTo(TERRAIN_TYPE_A)));
		assertThat(manager.getOrDefault(NAME_B), is(equalTo(TERRAIN_TYPE_B)));
		assertThat(manager.getOrDefault(NAME_C), is(equalTo(TERRAIN_TYPE_C)));

		verify(fileUtils).readWholeFile(file);
		verify(converter).load(MOCKED_JSON);
	}

	@Test
	public void testLoadWithIOException() throws IOException {
		File file = new File("test.json");

		when(fileUtils.readWholeFile(file)).thenThrow(new IOException("Test"));

		manager.load(file);

		assertThat(manager.size(), is(equalTo(0)));

		verify(fileUtils).readWholeFile(file);
		verify(converter, never()).load(MOCKED_JSON);
	}

	// save()

	@Test
	public void testSave() throws IOException {
		File file = new File("test.json");

		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		when(converter.save(anyCollection())).thenReturn(MOCKED_JSON);

		manager.save(file);

		verify(converter).save(anyCollection());
		verify(fileUtils).writeWholeFile(file, MOCKED_JSON);
	}

	@Test
	public void testSaveWithIOException() throws IOException {
		File file = new File("test.json");

		manager.add(TERRAIN_TYPE_A);
		manager.add(TERRAIN_TYPE_B);
		manager.add(TERRAIN_TYPE_C);

		when(converter.save(anyCollection())).thenReturn(MOCKED_JSON);
		doThrow(new IOException("testSaveWithIOException()")).when(fileUtils).writeWholeFile(file, MOCKED_JSON);

		assertThrows(IOException.class, () -> manager.save(file));

		verify(converter).save(anyCollection());
		verify(fileUtils).writeWholeFile(file, MOCKED_JSON);
	}
}