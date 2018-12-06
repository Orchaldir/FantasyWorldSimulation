package jfws.editor.map;

import javafx.stage.FileChooser;
import jfws.maps.sketch.SketchMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static javafx.scene.control.Alert.AlertType.ERROR;

@Slf4j
public class MenuBarController {

	private final MapStorage mapStorage;
	private final EditorController editorController;

	private final FileChooser mapChooser;
	private final FileChooser imageChooser;

	public MenuBarController(MapStorage mapStorage, EditorController editorController) {
		this.mapStorage = mapStorage;
		this.editorController = editorController;

		mapChooser = new FileChooser();
		imageChooser = new FileChooser();

		addExtension(mapChooser, "JSON", "*.json");
		setDirectory(mapChooser, "./data/map/");

		addExtension(imageChooser, "PNG", "*.png");
		setDirectory(imageChooser, ".");
	}

	public MenuBarController(MapStorage mapStorage, EditorController editorController,
							 FileChooser mapChooser, FileChooser imageChooser) {
		this.mapStorage = mapStorage;
		this.editorController = editorController;
		this.mapChooser = mapChooser;
		this.imageChooser = imageChooser;
	}

	private void addExtension(FileChooser fileChooser, String description, String extension) {
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
		fileChooser.getExtensionFilters().add(extFilter);
	}

	private void setDirectory(FileChooser fileChooser, String relativePath) {
		String currentPath = Paths.get(relativePath).toAbsolutePath().normalize().toString();
		fileChooser.setInitialDirectory(new File(currentPath));
	}

	public void loadMap() {
		File file = mapChooser.showOpenDialog(editorController.getWindow());

		if (file != null) {
			log.info("loadMap(): file={}", file.getPath());

			try {
				SketchMap sketchMap = mapStorage.getSketchConverter().load(file);
				mapStorage.setSketchMap(sketchMap);
				editorController.render();
			} catch (IOException e) {
				editorController.showAlert(ERROR, "Map loading failed!", e.getMessage());
			}
		}
		else {
			log.info("loadMap(): No file.");
		}
	}

	public void saveMap() {
		File file = mapChooser.showSaveDialog(editorController.getWindow());

		if (file != null) {
			log.info("saveMap(): file={}", file.getPath());

			try {
				mapStorage.getSketchConverter().save(file, mapStorage.getSketchMap());
			} catch (IOException e) {
				log.error("saveMap(): ", e);
			}
		}
		else {
			log.info("saveMap(): No file.");
		}
	}

	public void exportImage() {
		File file = imageChooser.showSaveDialog(editorController.getWindow());

		if (file != null) {
			log.info("exportImage(): file={}", file.getPath());
			BufferedImage bufferedImage = editorController.getSnapshot();

			try {
				editorController.saveSnapshot(file, bufferedImage);
			} catch (IOException e) {
				log.error("exportImage(): ", e);
			}
		}
		else {
			log.info("exportImage(): No file.");
		}
	}
}
