package jfws.feature.rpg.rules.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TraitManagerTest {

	private static final String NAME0 = "Trait0";
	private static final String NAME1 = "Trait1";
	private static final String NAME2 = "Trait2";

	private Trait trait0;
	private Trait trait1;
	private Trait trait2;

	private TraitManager manager;

	@BeforeEach
	public void setUp() {
		trait0 = mock(Trait.class);
		trait1 = mock(Trait.class);
		trait2 = mock(Trait.class);

		when(trait0.getName()).thenReturn(NAME0);
		when(trait1.getName()).thenReturn(NAME1);
		when(trait2.getName()).thenReturn(NAME2);

		manager = new TraitManager();
	}

	@Test
	public void testAdd() {
		manager.add(trait0);
		manager.add(trait1);
		manager.add(trait2);

		assertThat(manager.get(NAME0), is(trait0));
		assertThat(manager.get(NAME1), is(trait1));
		assertThat(manager.get(NAME2), is(trait2));
	}

	@Test
	public void testAddTwice() {
		manager.add(trait0);

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> manager.add(trait0));

		assertThat(exception.getMessage(), is("Trait 'Trait0' already exists!"));
	}

	@Test
	public void testAddNull() {
		assertThrows(NullPointerException.class, () -> manager.add(null));
	}

	@Test
	public void testGetNonExistingTrait() {
		NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> manager.get(NAME0));

		assertThat(exception.getMessage(), is("Did not find trait 'Trait0'!"));
	}

	@Test
	public void testAddAll() {
		manager.add(trait0);
		manager.add(trait1);
		manager.add(trait2);

		assertThat(manager.getAll(), is(List.of(trait0, trait1, trait2)));
	}

}