package jfws.util.ecs.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class ComponentMapTest {

	private static final Integer ID0 = 0;
	private static final Integer ID1 = 1;
	private static final Integer ID2 = 2;

	private static final Integer COMPONENT0 = 77;
	private static final Integer COMPONENT1 = 435;
	private static final Integer COMPONENT2 = 8797;

	private ComponentMap<Integer> componentMap;

	@BeforeEach
	public void setUp() {
		componentMap = new ComponentMap<>();
	}

	@Test
	public void testAddNotExistingComponent() {
		assertThat(componentMap.get(ID0), is(Optional.empty()));
	}

	@Test
	public void testAdd() {
		componentMap.add(ID0, COMPONENT0);
		componentMap.add(ID2, COMPONENT2);
		componentMap.add(ID1, COMPONENT1);

		assertThat(componentMap.get(ID0).get(), is(COMPONENT0));
		assertThat(componentMap.get(ID1).get(), is(COMPONENT1));
		assertThat(componentMap.get(ID2).get(), is(COMPONENT2));
	}

	@Test
	public void testRemove() {
		componentMap.add(ID0, COMPONENT0);

		assertThat(componentMap.get(ID0).get(), is(COMPONENT0));

		componentMap.remove(ID0);

		assertThat(componentMap.get(ID0), is(Optional.empty()));
	}

	@Test
	public void testGetIds() {
		componentMap.add(ID0, COMPONENT0);
		componentMap.add(ID2, COMPONENT2);
		componentMap.add(ID1, COMPONENT1);

		assertThat(componentMap.getIds(), is(Set.of(ID0, ID1, ID2)));
	}

}