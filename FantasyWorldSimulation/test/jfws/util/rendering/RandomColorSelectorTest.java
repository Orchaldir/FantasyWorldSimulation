package jfws.util.rendering;

import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.jgiven.junit5.ScenarioTest;
import jfws.util.rendering.bdd.RandomColorSelectorGiven;
import jfws.util.rendering.bdd.RandomColorSelectorThen;
import jfws.util.rendering.bdd.RandomColorSelectorWhen;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith( JGivenExtension.class )
class RandomColorSelectorTest extends ScenarioTest<RandomColorSelectorGiven, RandomColorSelectorWhen, RandomColorSelectorThen> {

	@Disabled
	@Test
	public void the_selected_color_is_random() {
		given().a_random_color_selector();

		when().a_color_is_selected();

		then().the_selected_color_is_random();
	}

}