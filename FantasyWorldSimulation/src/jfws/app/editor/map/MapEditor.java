package jfws.app.editor.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MapEditor extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		URL url = getClass().getResource("map-editor.fxml");
		Parent root = FXMLLoader.load(url);
		primaryStage.setTitle("Map Editor");
		primaryStage.setScene(new Scene(root, 1000, 800));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
