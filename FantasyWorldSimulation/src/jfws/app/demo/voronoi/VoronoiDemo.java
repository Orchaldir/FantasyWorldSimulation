package jfws.app.demo.voronoi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class VoronoiDemo extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception{
		URL url = getClass().getResource("voronoi.fxml");
		Parent root = FXMLLoader.load(url);
		primaryStage.setTitle("Voronoi Demo");
		primaryStage.setScene(new Scene(root, 1000, 800));
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
