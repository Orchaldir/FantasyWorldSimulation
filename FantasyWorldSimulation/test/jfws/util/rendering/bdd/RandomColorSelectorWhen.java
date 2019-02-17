package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import javafx.scene.paint.Color;
import jfws.util.rendering.RandomColorSelector;


public class RandomColorSelectorWhen extends Stage<RandomColorSelectorWhen> {

	@ExpectedScenarioState
	protected RandomColorSelector selector;

	@ProvidedScenarioState
	protected Color color;

	public RandomColorSelectorWhen a_color_is_selected() {
		color = selector.select(null);

		return this;
	}
}
