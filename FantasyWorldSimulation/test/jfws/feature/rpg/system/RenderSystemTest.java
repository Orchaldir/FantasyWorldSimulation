package jfws.feature.rpg.system;

import javafx.scene.paint.Color;
import jfws.feature.rpg.component.Pose;
import jfws.util.ecs.component.ComponentStorage;
import jfws.util.rendering.Renderer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.*;

class RenderSystemTest {

	private static final double SCALE = 11.7;
	private static final double POINT_SIZE = 0.5;

	private static final Pose POSE0 = new Pose(1.0, 2.0, 3.0);
	private static final Pose POSE1 = new Pose(-6.99, 342.4, -93.0);

	private ComponentStorage<Pose> poseStorage;
	private Renderer renderer;

	private RenderSystem renderSystem;

	@BeforeEach
	public void setUp() {
		poseStorage = mock(ComponentStorage.class);
		renderer = mock(Renderer.class);

		renderSystem = new RenderSystem(poseStorage, SCALE, POINT_SIZE);
	}

	@Test
	public void testRender() {
		when(poseStorage.getAll()).thenReturn(Set.of(POSE0, POSE1));

		renderSystem.render(renderer);

		verify(renderer, times(1)).setColor(Color.RED);
		verify(renderer, times(1)).setScale(SCALE);
		verify(renderer, times(1)).renderPoint(POSE0.position, POINT_SIZE);
		verify(renderer, times(1)).renderPoint(POSE1.position, POINT_SIZE);
		verifyNoMoreInteractions(renderer);
	}

}