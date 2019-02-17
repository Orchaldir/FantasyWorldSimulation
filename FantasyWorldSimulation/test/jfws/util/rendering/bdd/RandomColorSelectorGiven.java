package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import jfws.util.math.random.RandomNumberGenerator;
import jfws.util.rendering.RandomColorSelector;

import static org.mockito.Mockito.mock;

public class RandomColorSelectorGiven extends Stage<RandomColorSelectorGiven> {

	@ProvidedScenarioState
	protected RandomNumberGenerator generator;

	@ProvidedScenarioState
	protected RandomColorSelector selector;

	public RandomColorSelectorGiven a_random_color_selector() {
		generator = mock(RandomNumberGenerator.class);

		selector = new RandomColorSelector(generator);

		return this;
	}
}
