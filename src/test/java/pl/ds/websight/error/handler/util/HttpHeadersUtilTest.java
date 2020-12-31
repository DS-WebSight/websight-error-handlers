package pl.ds.websight.error.handler.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HttpHeadersUtilTest {

    private static final String DEFAULT_RESPONSE_TYPE = "text/html";
    private static final String APPLICATION_JSON_TYPE = "application/json";

    @Test
    void shouldReturnFalseeWithTextHtmlAndApplicationJsonWithTheSameQuality() {
        String acceptHeader = "text/html,application/json,application/xml;q=0.9,image/webp";
        assertEquals(false, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnTrueWithTextAnyAndApplicationJsonWithTheSameQuality() {
        String acceptHeader = "text/*,application/json,application/xml;q=0.9,image/webp";
        assertEquals(true, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnFalseWithTextHtmlAndApplicationAnyWithTheSameQuality() {
        String acceptHeader = "text/html,application/*,application/xml;q=0.9,image/webp";
        assertEquals(false, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnFalseWithTextAnyAndApplicationAnyWithTheSameQuality() {
        String acceptHeader = "text/*,application/*,application/xml;q=0.9,image/webp";
        assertEquals(false, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnTrueWithAnyAnyAndApplicationAnyWithTheSameQuality() {
        String acceptHeader = "*/*,application/*,application/xml;q=0.9,image/webp";
        assertEquals(true, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnTrueWithTextHtmlAndApplicationJsonWithBiggerQuality() {
        String acceptHeader = "text/html;q=0.9,application/json,application/xml;q=0.9,image/webp";
        assertEquals(true, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnFalseeWithTextHtmlAndApplicationAnyWithBiggerQuality() {
        String acceptHeader = "text/html;q=0.9,application/*,application/xml;q=0.9,image/webp";
        assertEquals(true, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnFalseWithTextAnyWithBiggerQualityAndApplicationJson() {
        String acceptHeader = "text/*,application/json;q=0.9,application/xml;q=0.9,image/webp";
        assertEquals(false, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }

    @Test
    void shouldReturnFalseeWithTextAnyWithBiggerQualityAndApplicationJson() {
        String acceptHeader = "text/*,application/json;q=0.9,application/xml;q=0.9,image/webp";
        assertEquals(false, HttpHeadersUtil.isMimeTypeAccepted(acceptHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE));
    }
}
