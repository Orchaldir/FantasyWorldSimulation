package jfws.util.rendering;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

class ColorSelectorMapTest {

	private static final String NAME0 = "A", NAME1 = "B", NAME2 = "C";

	private ColorSelector<Integer> colorSelector0, colorSelector1, colorSelector2;
	private ColorSelectorMap<Integer> colorSelectorMap;

	@BeforeEach
	void setup() {
		colorSelector0 = Mockito.mock(ColorSelector.class);
		colorSelector1 = Mockito.mock(ColorSelector.class);
		colorSelector2 = Mockito.mock(ColorSelector.class);

		when(colorSelector0.getName()).thenReturn(NAME0);
		when(colorSelector1.getName()).thenReturn(NAME1);
		when(colorSelector2.getName()).thenReturn(NAME2);

		colorSelectorMap = new ColorSelectorMap<>(colorSelector0);
	}

	@Test
	void testGetDefault() {
		assertThat(colorSelectorMap.getDefaultColorSelector(), is(equalTo(colorSelector0)));
	}

	@Test
	void testGetWithoutAdd() {
		assertThat(colorSelectorMap.get(NAME0), is(equalTo(colorSelector0)));
		assertThat(colorSelectorMap.get(NAME1), is(equalTo(colorSelector0)));
		assertThat(colorSelectorMap.get(NAME2), is(equalTo(colorSelector0)));
	}

	@Test
	void test() {
		colorSelectorMap.add(colorSelector1);
		colorSelectorMap.add(colorSelector2);

		assertThat(colorSelectorMap.get(NAME0), is(equalTo(colorSelector0)));
		assertThat(colorSelectorMap.get(NAME1), is(equalTo(colorSelector1)));
		assertThat(colorSelectorMap.get(NAME2), is(equalTo(colorSelector2)));
	}

	// getNames()

	@Test
	void testGetNames() {
		colorSelectorMap.add(colorSelector1);
		colorSelectorMap.add(colorSelector2);

		assertThat(colorSelectorMap.getNames(), containsInAnyOrder(NAME0, NAME1, NAME2));
	}

}