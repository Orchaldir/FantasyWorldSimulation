package jfws.util.ecs.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ComponentUtilTest {

	public static final Set<Integer> IDS_1_2_3 = Set.of(1, 2, 3);
	public static final Set<Integer> IDS_4_5_6 = Set.of(4, 5 ,6);
	public static final Set<Integer> IDS_7 = Set.of(7);
	public static final Set<Integer> IDS_2_4_6 = Set.of( 2, 4, 6);
	public static final Set<Integer> IDS_2_20_200 = Set.of(2, 20_200);

	private ComponentStorage<Boolean> storage0;
	private ComponentStorage<Integer> storage1;
	private ComponentStorage<String> storage2;

	@BeforeEach
	public void setUp() {
		storage0 = mock(ComponentStorage.class);
		storage1 = mock(ComponentStorage.class);
		storage2 = mock(ComponentStorage.class);
	}

	@Test
	public void testGetSharedIdsWithNoStorages() {
		assertThat(ComponentUtil.getSharedIds(Collections.emptyList()), is(empty()));
	}

	@Test
	public void testGetSharedIdsWithOneStorage() {
		when(storage0.getIds()).thenReturn(IDS_1_2_3);

		assertThat(ComponentUtil.getSharedIds(List.of(storage0)), is(IDS_1_2_3));
	}

	@Test
	public void testGetSharedIdsWithSameIds() {
		when(storage0.getIds()).thenReturn(IDS_1_2_3);
		when(storage1.getIds()).thenReturn(IDS_1_2_3);
		when(storage2.getIds()).thenReturn(IDS_1_2_3);

		assertThat(ComponentUtil.getSharedIds(List.of(storage0, storage1, storage2)), is(IDS_1_2_3));
	}

	@Test
	public void testGetSharedIdsWithSomeSharedIds() {
		when(storage0.getIds()).thenReturn(IDS_1_2_3);
		when(storage1.getIds()).thenReturn(IDS_2_4_6);
		when(storage2.getIds()).thenReturn(IDS_2_20_200);

		assertThat(ComponentUtil.getSharedIds(List.of(storage0, storage1, storage2)), is(Set.of(2)));
	}

	@Test
	public void testGetSharedIdsWithNoSharedIds() {
		when(storage0.getIds()).thenReturn(IDS_1_2_3);
		when(storage1.getIds()).thenReturn(IDS_4_5_6);
		when(storage2.getIds()).thenReturn(IDS_7);

		assertThat(ComponentUtil.getSharedIds(List.of(storage0, storage1, storage2)), is(empty()));
	}

}