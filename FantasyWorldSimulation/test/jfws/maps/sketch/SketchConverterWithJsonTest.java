package jfws.maps.sketch;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;

import static jfws.maps.sketch.SketchConverterWithJson.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class SketchConverterWithJsonTest {

	private SketchConverterWithJson converter;

	@BeforeEach
	void setup() {
		converter = new SketchConverterWithJson(null, null);
	}

	private <T extends Throwable> void assertException(Class<T> expectedType, Executable executable, String message) {
		Throwable throwable = assertThrows(expectedType, executable);
		assertThat(throwable.getMessage(), is(equalTo(message)));
	}

	// load

	// save

	@Test
	void testParseStringWithNull() {
		assertThrows(IOException.class, () -> converter.parseString(null));
	}

	@Test
	void testParseEmptyString() {
		assertException(IOException.class, () -> converter.parseString(""), NOT_A_JSON_OBJECT);
	}

	@Test
	void testParseWrongString() {
		assertException(IOException.class, () -> converter.parseString("abc"), NOT_A_JSON_OBJECT);
	}

	// version

	// "{\"width\":1,\"height\":10,\"used_terrain_types\":[\"A\"],\"terrain_type_map\":[\"0\"]}"

	@Test
	void testNoVersion() {
		assertException(IOException.class, () -> converter.parseString("{}"), NO_VERSION);
	}

	@Test
	void testWrongVersion() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":0}"), WRONG_VERSION);
	}

	@Test
	void testWrongVersionFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":A}"), WRONG_VERSION_FORMAT);
	}

	// map size

	@Test
	void testNoWidth() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1}"), NO_SIZE);
	}

	@Test
	void testWrongWidthFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":A}"), WRONG_SIZE_FORMAT);
	}

	@Test
	void tesNoHeight() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":1}"), NO_SIZE);
	}

	@Test
	void testWrongHeightFormat() {
		assertException(IOException.class, () -> converter.parseString("{\"version\":1,\"width\":2,\"height\":A}"), WRONG_SIZE_FORMAT);
	}

}