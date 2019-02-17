package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import com.tngtech.jgiven.annotation.ScenarioState;
import javafx.scene.paint.Color;
import jfws.util.math.random.RandomNumberGenerator;
import jfws.util.rendering.RandomColorSelector;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;


public class RandomColorSelectorWhen extends Stage<RandomColorSelectorWhen> {

	@ExpectedScenarioState
	protected RandomNumberGenerator generator;

	@ExpectedScenarioState
	protected RandomColorSelector selector;

	@ProvidedScenarioState(resolution = ScenarioState.Resolution.NAME)
	protected Color color0;

	@ProvidedScenarioState(resolution = ScenarioState.Resolution.NAME)
	protected Color color1;

	public RandomColorSelectorWhen a_color_is_selected() {
		color0 = selector.select(null);

		return this;
	}

	public RandomColorSelectorWhen a_second_color_is_selected() {
		color1 = selector.select(null);

		return this;
	}

	public RandomColorSelectorWhen the_selector_is_reset() {
		selector.reset();

		return this;
	}
}
