package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
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

	@ExpectedScenarioState(resolution = ScenarioState.Resolution.NAME)
	protected Color color0;

	@ExpectedScenarioState(resolution = ScenarioState.Resolution.NAME)
	protected Color color1;

	public RandomColorSelectorThen the_selected_color_is_random() {
		assertColor(color0, 1.0, 0.5, 0.0);
		return this;
	}

	public RandomColorSelectorThen the_second_color_is_different() {
		assertColor(color1, 0.25, 1.0, 0.75);
		return this;
	}

	public RandomColorSelectorThen the_generator_was_called_$_times(int n) {
		verify(generator, times(n)).getInteger(MAX_VALUE);
		return this;
	}

	public RandomColorSelectorThen the_generator_was_reset_$_times(int n) {
		verify(generator, times(n)).restart();
		return this;
	}

	private void assertColor(Color color, double red, double green, double blue) {
		assertNotNull(color);

		assertThat(color.getRed(),   is(closeTo(red, ERROR)));
		assertThat(color.getGreen(), is(closeTo(green, ERROR)));
		assertThat(color.getBlue(),  is(closeTo(blue, ERROR)));
	}
}
