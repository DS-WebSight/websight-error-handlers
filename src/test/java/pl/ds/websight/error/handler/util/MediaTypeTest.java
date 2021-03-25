package pl.ds.websight.error.handler.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MediaTypeTest {

    private static MediaType mediaType;

    @BeforeAll
    static void setUp() {
        mediaType = new MediaType("application/json;q=0.9;v=b3");
    }

    @Test
    void shouldGetType() {
        assertEquals("application", mediaType.getType());
    }

    @Test
    void shouldGetSubtype() {
        assertEquals("json", mediaType.getSubtype());
    }

    @Test
    void shouldGetParameters() {
        Map<String, String> parameters = mediaType.getParameters();

        assertEquals(2, parameters.size());
        assertEquals("0.9", parameters.get("q"));
        assertEquals("b3", parameters.get("v"));
    }
}
