package jfws.editor.map;

import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import jfws.maps.sketch.SketchConverter;
import jfws.maps.sketch.SketchMap;
import jfws.util.command.CommandHistory;
import jfws.util.io.FileUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

class MenuBarControllerTest {

	public static final String MAP_PATH = "map_path";
	public static final String IMAGE_PATH = "image_path";

	private MapStorage mapStorage;
	private SketchConverter sketchConverter;
	private SketchMap sketchMap;

	private FileUtils fileUtils;
	private CommandHistory commandHistory;

	private EditorController editorController;
	private Window window;
	private MenuItem undoItem;
	private MenuItem redoItem;
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

		fileUtils = mock(FileUtils.class);
		commandHistory = mock(CommandHistory.class);

		editorController = mock(EditorController.class);
		window = mock(Window.class);
		undoItem = mock(MenuItem.class);
		redoItem = mock(MenuItem.class);
		bufferedImage = mock(BufferedImage.class);

		mapChooser = mock(FileChooser.class);
		imageChooser = mock(FileChooser.class);
		file = mock(File.class);

		menuBarController = new MenuBarController(mapStorage, editorController, mapChooser, imageChooser, undoItem, redoItem);
	}

	// constructor

	@Test
	public void testConstructor() throws IOException {
		when(mapStorage.getFileUtils()).thenReturn(fileUtils);
		when(fileUtils.getAbsolutePath(MenuBarController.MAP_PATH)).thenReturn(MAP_PATH);
		when(fileUtils.getAbsolutePath(MenuBarController.IMAGE_PATH)).thenReturn(IMAGE_PATH);

		menuBarController = new MenuBarController(mapStorage, editorController, undoItem, redoItem);

		assertFileChooser(menuBarController.getMapChooser(), MenuBarController.MAP_DESCRIPTION, MenuBarController.MAP_EXTENSION, MAP_PATH);
		assertFileChooser(menuBarController.getImageChooser(), MenuBarController.IMAGE_DESCRIPTION, MenuBarController.IMAGE_EXTENSION, IMAGE_PATH);
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

	// history

	@Test
	public void testUndo() {
		when(mapStorage.getCommandHistory()).thenReturn(commandHistory);

		menuBarController.undo();

		verify(mapStorage, atLeastOnce()).getCommandHistory();
		verify(commandHistory).unExecute();
		verify(commandHistory, never()).reExecute();
		verify(commandHistory).canUnExecute();
		verify(commandHistory).canReExecute();
		verify(editorController).render();
	}

	@Test
	public void testRedo() {
		when(mapStorage.getCommandHistory()).thenReturn(commandHistory);

		menuBarController.redo();

		verify(mapStorage, atLeastOnce()).getCommandHistory();
		verify(commandHistory, never()).unExecute();
		verify(commandHistory).reExecute();
		verify(commandHistory).canUnExecute();
		verify(commandHistory).canReExecute();
		verify(editorController).render();
	}

	@Test
	public void testUpdateHistoryWithNoCommand() {
		testUpdateHistory(false, false);
	}

	@Test
	public void testUpdateHistoryWithUndo() {
		testUpdateHistory(true, false);
	}

	@Test
	public void testUpdateHistoryWithRedo() {
		testUpdateHistory(false, true);
	}

	@Test
	public void testUpdateHistoryWithBoth() {
		testUpdateHistory(true, true);
	}

	private void testUpdateHistory(boolean canUndo, boolean canRedo) {
		when(mapStorage.getCommandHistory()).thenReturn(commandHistory);
		when(commandHistory.canUnExecute()).thenReturn(canUndo);
		when(commandHistory.canReExecute()).thenReturn(canRedo);

		menuBarController.updateHistory();

		verify(commandHistory).canUnExecute();
		verify(commandHistory).canReExecute();
		verify(undoItem).setDisable(!canUndo);
		verify(redoItem).setDisable(!canRedo);
	}

	//

	private void assertFileChooser(FileChooser fileChooser, String description, String extension, String path) {
		assertThat(fileChooser.getExtensionFilters(), hasItem(allOf(
				hasProperty("description", is(description)),
				hasProperty("extensions", hasItem(extension))
		)));
		assertThat(fileChooser.getInitialDirectory().getPath(), is(path));
	}

}