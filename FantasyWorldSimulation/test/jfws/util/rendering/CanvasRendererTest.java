package jfws.util.rendering;

import com.tngtech.jgiven.junit5.JGivenExtension;
import com.tngtech.jgiven.junit5.ScenarioTest;
import jfws.util.rendering.bdd.CanvasRendererGiven;
import jfws.util.rendering.bdd.CanvasRendererThen;
import jfws.util.rendering.bdd.CanvasRendererWhen;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith( JGivenExtension.class )
class CanvasRendererTest
		extends ScenarioTest<CanvasRendererGiven, CanvasRendererWhen, CanvasRendererThen> {

	@Test
	public void the_canvas_can_be_cleared() {
		given().a_canvas_renderer();

		when().the_renderer_is_cleared(1 , 2, 300, 400);

		then().the_canvas_is_cleared(1 , 2, 300, 400);
	}

	@Test
	public void the_scale_of_the_canvas_can_be_set() {
		given().a_canvas_renderer();

		when().the_scale_of_the_renderer_is_set_to(1.5);

		then().the_scale_of_the_canvas_is_set_to(1.5);
	}

	@Test
	public void the_render_color_can_be_set() {
		given().a_canvas_renderer().
			and().a_color();

		when().the_render_color_is_set();

		then().the_fill_color_of_the_canvas_is_set();
	}

	@Test
	public void a_rectangle_can_be_rendered() {
		given().a_canvas_renderer();

		when().a_rectangle_is_rendered_with_the_renderer(1 , 2, 300, 400);

		then().a_rectangle_is_rendered_on_the_canvas(1 , 2, 300, 400);
	}

	@Test
	public void a_polygon_can_be_rendered() {
		given().a_canvas_renderer().
			and().a_polygon();

		when().a_polygon_is_rendered_with_the_renderer();

		then().a_polygon_is_rendered_on_the_canvas();
	}
}