package jfws.feature.world.attribute;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AttributeColorSelectorTest {

	public static final String NAME = "test";
	public static final int INDEX = 77;

	private AttributeColorSelector selector;

	private AttributeLevelUtility utility;
	private AttributeCell cell;

	@BeforeEach
	public void setUp() {
		utility = mock(AttributeLevelUtility.class);
		cell = mock(AttributeCell.class);

		selector = new AttributeColorSelector(NAME, utility, INDEX);
	}

	@Test
	public void testGetName() {
		assertThat(selector.getName(), is(equalTo(NAME)));
	}

	@Test
	public void testSelect() {
		when(cell.getAttribute(INDEX)).thenReturn(123.0);
		when(utility.getColor(123.0)).thenReturn(Color.BLUEVIOLET);

		assertThat(selector.select(cell), is((equalTo(Color.BLUEVIOLET))));

		verify(cell, times(1)).getAttribute(INDEX);
		verify(utility, times(1)).getColor(123.0);
	}
}