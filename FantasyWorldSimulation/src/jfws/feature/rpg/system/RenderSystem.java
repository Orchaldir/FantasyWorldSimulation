package jfws.feature.rpg.system;

import javafx.scene.paint.Color;
import jfws.feature.rpg.component.Pose;
import jfws.util.ecs.component.ComponentStorage;
import jfws.util.rendering.Renderer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@AllArgsConstructor
@Slf4j
public class RenderSystem {

	private final ComponentStorage<Pose> poseStorage;
	private double scale;
	private double pointSize;

	public void render(Renderer renderer) {
		renderer.setColor(Color.RED);
		renderer.setScale(scale);

		Collection<Pose> poses = poseStorage.getAll();

		log.info("render(): poses={}", poses.size());

		for (Pose pose : poses) {
			log.info("render(): {}", pose);
			pointSize = 0.5;
			renderer.renderPoint(pose.position, pointSize);
		}
	}
}
