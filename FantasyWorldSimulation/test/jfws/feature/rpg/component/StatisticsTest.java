package jfws.feature.rpg.component;

import jfws.feature.rpg.rules.unit.Trait;
import jfws.util.ecs.component.ComponentStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static jfws.feature.rpg.component.Statistics.DEFAULT_RANK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatisticsTest {

	private static final String NAME0 = "Trait0";
	private static final String NAME1 = "Trait1";
	private static final String NAME2 = "Trait2";

	private static final int RANK0 = 4;
	private static final int RANK1 = -2;
	private static final int RANK2 = 9;

	public static final int ENTITY_ID = 42;

	private Trait trait0;
	private Trait trait1;
	private Trait trait2;

	private Statistics statistics;

	@BeforeEach
	public void setUp() {
		trait0 = mock(Trait.class);
		trait1 = mock(Trait.class);
		trait2 = mock(Trait.class);

		when(trait0.getName()).thenReturn(NAME0);
		when(trait1.getName()).thenReturn(NAME1);
		when(trait2.getName()).thenReturn(NAME2);

		statistics = new Statistics();
	}

	@Test
	public void testAdd() {
		statistics.setRank(trait0, RANK0);
		statistics.setRank(trait1, RANK1);
		statistics.setRank(trait2, RANK2);

		assertThat(statistics.getRank(trait0), is(RANK0));
		assertThat(statistics.getRank(trait1), is(RANK1));
		assertThat(statistics.getRank(trait2), is(RANK2));
	}

	@Test
	public void testGetDefaultRank() {
		assertThat(statistics.getRank(trait0), is(DEFAULT_RANK));
	}

	@Test
	public void testGetRankFromStorage() {
		ComponentStorage<Statistics> storage = mock(ComponentStorage.class);

		when(storage.get(ENTITY_ID)).thenReturn(Optional.of(statistics));
		statistics.setRank(trait0, RANK0);

		assertThat(Statistics.getRank(storage, ENTITY_ID,  trait0), is(RANK0));
	}

	@Test
	public void testGetDefaultRankFromStorage() {
		ComponentStorage<Statistics> storage = mock(ComponentStorage.class);

		when(storage.get(ENTITY_ID)).thenReturn(Optional.empty());

		assertThat(Statistics.getRank(storage, ENTITY_ID,  trait0), is(DEFAULT_RANK));
	}

}