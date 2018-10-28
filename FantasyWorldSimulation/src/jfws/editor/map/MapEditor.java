package jfws.editor.map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MapEditor extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		Parent root = FXMLLoader.load(getClass().getResource("map-editor.fxml"));
		primaryStage.setTitle("Map Editor");
		primaryStage.setScene(new Scene(root, 600, 450));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
