package jfws.editor.map;

import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jfws.maps.sketch.SketchConverter;
import jfws.maps.sketch.SketchMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

class MenuBarControllerTest {

	private MapStorage mapStorage;
	private SketchConverter sketchConverter;
	private SketchMap sketchMap;

	private EditorController editorController;
	private Window window;
	private BufferedImage bufferedImage;

	private FileChooser mapChooser;
	private FileChooser imageChooser;
	private File file;

	private MenuBarController menuBarController;

	@BeforeEach
	public void setUp() {
		mapStorage = mock(MapStorage.class);
		sketchConverter = mock(SketchConverter.class);
		sketchMap = mock(SketchMap.class);

		editorController = mock(EditorController.class);
		window = mock(Window.class);
		bufferedImage = mock(BufferedImage.class);

		mapChooser = mock(FileChooser.class);
		imageChooser = mock(FileChooser.class);
		file = mock(File.class);

		menuBarController = new MenuBarController(mapStorage, editorController, mapChooser, imageChooser);
	}

	// constructor

	@Test
	public void testConstructor() throws IOException {
		menuBarController = new MenuBarController(mapStorage, editorController);
	}

	// loadMap()

	@Test
	public void testLoadMapWithNoFile() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(mapChooser.showOpenDialog(window)).thenReturn(null);

		menuBarController.loadMap();

		verify(editorController).getWindow();
		verify(mapChooser).showOpenDialog(window);

		verify(mapStorage, never()).getSketchConverter();
		verify(mapStorage, never()).setSketchMap(any());
		verify(sketchConverter, never()).load(file);
		verify(editorController, never()).render();
		verify(editorController, never()).showAlert(any(), anyString(), anyString());
	}

	@Test
	public void testLoadMapFails() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(mapChooser.showOpenDialog(window)).thenReturn(file);
		when(mapStorage.getSketchConverter()).thenReturn(sketchConverter);
		when(sketchConverter.load(file)).thenThrow(new IOException(""));

		menuBarController.loadMap();

		verify(editorController).getWindow();
		verify(mapChooser).showOpenDialog(window);
		verify(mapStorage).getSketchConverter();
		verify(sketchConverter).load(file);
		verify(editorController).showAlert(eq(Alert.AlertType.ERROR), anyString(), anyString());

		verify(mapStorage, never()).setSketchMap(any());
		verify(editorController, never()).render();
	}

	@Test
	public void testLoadMap() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(mapChooser.showOpenDialog(window)).thenReturn(file);
		when(mapStorage.getSketchConverter()).thenReturn(sketchConverter);
		when(sketchConverter.load(file)).thenReturn(sketchMap);

		menuBarController.loadMap();

		verify(editorController).getWindow();
		verify(mapChooser).showOpenDialog(window);
		verify(mapStorage).getSketchConverter();
		verify(sketchConverter).load(file);
		verify(mapStorage).setSketchMap(sketchMap);
		verify(editorController).render();

		verify(editorController, never()).showAlert(any(), anyString(), anyString());
	}

	// saveMap()

	@Test
	public void testSaveMapWithNoFile() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(mapChooser.showSaveDialog(window)).thenReturn(null);

		menuBarController.saveMap();

		verify(editorController).getWindow();
		verify(mapChooser).showSaveDialog(window);

		verify(mapStorage, never()).getSketchConverter();
		verify(mapStorage, never()).getSketchMap();
		verify(mapStorage, never()).setSketchMap(any());
		verify(sketchConverter, never()).save(eq(file), eq(sketchMap));
		verify(editorController, never()).render();
		verify(editorController, never()).showAlert(any(), anyString(), anyString());
	}

	@Test
	public void testSaveMapFails() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(mapChooser.showSaveDialog(window)).thenReturn(file);
		when(mapStorage.getSketchConverter()).thenReturn(sketchConverter);
		when(mapStorage.getSketchMap()).thenReturn(sketchMap);
		doThrow(new IOException("")).when(sketchConverter).save(eq(file), eq(sketchMap));

		menuBarController.saveMap();

		verify(editorController).getWindow();
		verify(mapChooser).showSaveDialog(window);
		verify(mapStorage).getSketchConverter();
		verify(mapStorage).getSketchMap();
		verify(sketchConverter).save(eq(file), eq(sketchMap));


		verify(mapStorage, never()).setSketchMap(any());
		verify(editorController, never()).render();
		verify(editorController, never()).showAlert(any(), anyString(), anyString());
	}

	@Test
	public void testSaveMap() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(mapChooser.showSaveDialog(window)).thenReturn(file);
		when(mapStorage.getSketchConverter()).thenReturn(sketchConverter);
		when(mapStorage.getSketchMap()).thenReturn(sketchMap);

		menuBarController.saveMap();

		verify(editorController).getWindow();
		verify(mapChooser).showSaveDialog(window);
		verify(mapStorage).getSketchConverter();
		verify(mapStorage).getSketchMap();
		verify(sketchConverter).save(eq(file), eq(sketchMap));


		verify(mapStorage, never()).setSketchMap(any());
		verify(editorController, never()).render();
		verify(editorController, never()).showAlert(any(), anyString(), anyString());
	}

	// exportImage()

	@Test
	public void testExportImageWithNoFile() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(imageChooser.showSaveDialog(window)).thenReturn(null);

		menuBarController.exportImage();

		verify(editorController).getWindow();
		verify(imageChooser).showSaveDialog(window);

		verify(mapStorage, never()).getSketchConverter();
		verify(mapStorage, never()).getSketchMap();
		verify(mapStorage, never()).setSketchMap(any());
		verify(sketchConverter, never()).load(eq(file));
		verify(sketchConverter, never()).save(eq(file), eq(sketchMap));
		verify(editorController, never()).render();
		verify(editorController, never()).showAlert(any(), anyString(), anyString());
		verify(editorController, never()).getSnapshot();
	}

	@Test
	public void testExportImageFails() throws IOException {
		when(editorController.getWindow()).thenReturn(window);
		when(imageChooser.showSaveDialog(window)).thenReturn(file);
		when(editorController.getSnapshot()).thenReturn(bufferedImage);
		doThrow(new IOException("")).when(editorController).saveSnapshot(eq(file), eq(bufferedImage));

		menuBarController.exportImage();

		verify(editorController).getWindow();
		verify(imageChooser).showSaveDialog(window);
		verify(editorController).getSnapshot();
		verify(editorController).saveSnapshot(eq(file), eq(bufferedImage));

		verify(mapStorage, never()).getSketchConverter();
		verify(mapStorage, never()).getSketchMap();
		verify(mapStorage, never()).setSketchMap(any());
		verify(sketchConverter, never()).load(eq(file));
		verify(sketchConverter, never()).save(eq(file), eq(sketchMap));
		verify(editorController, never()).render();
		verify(editorController, never()).showAlert(any(), anyString(), anyString());
	}

}