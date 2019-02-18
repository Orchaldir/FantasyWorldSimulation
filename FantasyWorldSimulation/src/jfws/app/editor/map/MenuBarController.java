package jfws.app.editor.map;

import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import jfws.maps.sketch.SketchMap;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javafx.scene.control.Alert.AlertType.ERROR;

@Slf4j
public class MenuBarController {

	public static final String MAP_PATH = "data/map/";
	public static final String IMAGE_PATH = ".";
	public static final String MAP_EXTENSION = "*.json";
	public static final String IMAGE_EXTENSION = "*.png";
	public static final String MAP_DESCRIPTION = "JSON";
	public static final String IMAGE_DESCRIPTION = "PNG";

	private final MapStorage mapStorage;
	private final EditorController editorController;

	@Getter
	private final FileChooser mapChooser;
	@Getter
	private final FileChooser imageChooser;

	private final MenuItem undoItem;
	private final MenuItem redoItem;

	public MenuBarController(MapStorage mapStorage, EditorController editorController,
							 MenuItem undoItem, MenuItem redoItem) {
		this.mapStorage = mapStorage;
		this.editorController = editorController;
		this.undoItem = undoItem;
		this.redoItem = redoItem;

		mapChooser = new FileChooser();
		imageChooser = new FileChooser();

		addExtension(mapChooser, MAP_DESCRIPTION, MAP_EXTENSION);
		setDirectory(mapChooser, MAP_PATH);

		addExtension(imageChooser, IMAGE_DESCRIPTION, IMAGE_EXTENSION);
		setDirectory(imageChooser, IMAGE_PATH);
	}

	public MenuBarController(MapStorage mapStorage, EditorController editorController,
							 FileChooser mapChooser, FileChooser imageChooser,
							 MenuItem undoItem, MenuItem redoItem) {
		this.mapStorage = mapStorage;
		this.editorController = editorController;
		this.mapChooser = mapChooser;
		this.imageChooser = imageChooser;
		this.undoItem = undoItem;
		this.redoItem = redoItem;
	}

	private void addExtension(FileChooser fileChooser, String description, String extension) {
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
		fileChooser.getExtensionFilters().add(extFilter);
	}

	private void setDirectory(FileChooser fileChooser, String relativePath) {
		String currentPath = mapStorage.getFileUtils().getAbsolutePath(relativePath);
		fileChooser.setInitialDirectory(new File(currentPath));
	}

	// map

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

	// history

	public void undo() {
		log.info("undo()");
		mapStorage.getCommandHistory().unExecute();
		updateHistory();
		editorController.render();
	}

	public void redo() {
		log.info("redo()");
		mapStorage.getCommandHistory().reExecute();
		updateHistory();
		editorController.render();
	}

	public void updateHistory() {
		undoItem.setDisable(!mapStorage.getCommandHistory().canUnExecute());
		redoItem.setDisable(!mapStorage.getCommandHistory().canReExecute());
	}
}
