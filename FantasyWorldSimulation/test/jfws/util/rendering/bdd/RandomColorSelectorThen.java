package jfws.util.rendering.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ExpectedScenarioState;
import javafx.scene.paint.Color;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RandomColorSelectorThen extends Stage<RandomColorSelectorThen> {

	@ExpectedScenarioState
	protected Color color;

	public RandomColorSelectorThen the_selected_color_is_random() {
		assertNotNull(color);

		return this;
	}
}
