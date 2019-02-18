package jfws.app.editor.map;

import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public interface EditorController {

	BufferedImage getSnapshot();

	void saveSnapshot(File file, BufferedImage bufferedImage) throws IOException;

	Window getWindow();

	void render();

	void showAlert(Alert.AlertType type, String title, String content);
}
