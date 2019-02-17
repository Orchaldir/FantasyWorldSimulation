package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import javafx.scene.paint.Color;
import jfws.util.math.random.RandomNumberGenerator;

import static jfws.util.rendering.RandomColorSelector.MAX_VALUE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RandomColorSelectorThen extends Stage<RandomColorSelectorThen> {

	public static final double ERROR = 0.01;

	@ExpectedScenarioState
	protected RandomNumberGenerator generator;

	@ExpectedScenarioState
	protected Color color;

	public RandomColorSelectorThen the_selected_color_is_random() {
		assertNotNull(color);

		assertThat(color.getRed(),   is(closeTo(1.0, ERROR)));
		assertThat(color.getGreen(), is(closeTo(0.5, ERROR)));
		assertThat(color.getBlue(),  is(closeTo(0.0, ERROR)));

		verify(generator, times(3)).getInteger(MAX_VALUE);

		return this;
	}
}
