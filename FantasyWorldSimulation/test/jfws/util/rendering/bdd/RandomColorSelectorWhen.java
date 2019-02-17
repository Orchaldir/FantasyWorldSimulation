package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
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

	@ProvidedScenarioState
	protected Color color;

	public RandomColorSelectorWhen a_color_is_selected() {
		Mockito.when(generator.getInteger(anyInt())).thenReturn(255, 128, 0);

		color = selector.select(null);

		return this;
	}
}
