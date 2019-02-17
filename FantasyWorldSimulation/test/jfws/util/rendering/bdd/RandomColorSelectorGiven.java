package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import jfws.util.math.random.RandomNumberGenerator;
import jfws.util.rendering.RandomColorSelector;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;

public class RandomColorSelectorGiven extends Stage<RandomColorSelectorGiven> {

	@ProvidedScenarioState
	protected RandomNumberGenerator generator;

	@ProvidedScenarioState
	protected RandomColorSelector selector;

	public RandomColorSelectorGiven a_random_color_selector() {
		generator = mock(RandomNumberGenerator.class);

		Mockito.when(generator.getInteger(anyInt())).thenReturn(255, 128, 0, 64, 255, 193);

		selector = new RandomColorSelector(generator);

		return this;
	}
}
