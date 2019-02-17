package jfws.util.rendering;

import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.jgiven.junit5.ScenarioTest;
import jfws.util.rendering.bdd.RandomColorSelectorGiven;
import jfws.util.rendering.bdd.RandomColorSelectorThen;
import jfws.util.rendering.bdd.RandomColorSelectorWhen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith( JGivenExtension.class )
class RandomColorSelectorTest extends ScenarioTest<RandomColorSelectorGiven, RandomColorSelectorWhen, RandomColorSelectorThen> {

	@Test
	public void the_selected_color_is_random() {
		given().a_random_color_selector();

		when().a_color_is_selected();

		then().the_selected_color_is_random().
			and().the_generator_was_called_$_times(3).
			and().the_generator_was_reset_$_times(0);
	}

	@Test
	public void the_second_selected_color_is_different() {
		given().a_random_color_selector();

		when().a_color_is_selected().
			and().a_second_color_is_selected();

		then().the_selected_color_is_random().
			and().the_second_color_is_different().
			and().the_generator_was_called_$_times(6).
			and().the_generator_was_reset_$_times(0);
	}

	@Test
	public void the_generator_is_restarted_if_the_selector_is_reset() {
		given().a_random_color_selector();

		when().the_selector_is_reset();

		then().the_generator_was_called_$_times(0).
			and().the_generator_was_reset_$_times(1);
	}

}