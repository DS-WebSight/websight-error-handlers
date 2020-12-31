package pl.ds.websight.error.handler.servlet;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.sling.api.SlingConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.jetbrains.annotations.NotNull;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.ds.websight.error.handler.util.HttpHeadersUtil;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

@Component(service = Servlet.class)
@SlingServletPaths("sling/servlet/errorhandler/default")
public class ErrorServlet extends SlingAllMethodsServlet {

    private static final Logger LOG = LoggerFactory.getLogger(ErrorServlet.class);

    private static final String HTML_FILE_PATH = "/index.html";
    private static final String DEFAULT_RESPONSE_TYPE = "text/html";
    private static final String APPLICATION_JSON_TYPE = "application/json";

    private static final ObjectWriter JSON_WRITER = new ObjectMapper()
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .writer();

    @Override
    protected void service(@NotNull SlingHttpServletRequest request, @NotNull SlingHttpServletResponse response) throws IOException {
        Object errorStatus = request.getAttribute(SlingConstants.ERROR_STATUS);
        int status = (errorStatus != null) ? Integer.parseInt(errorStatus.toString()) : 500;

        Object errorMessage = request.getAttribute(SlingConstants.ERROR_MESSAGE);
        String message = (errorMessage != null) ? errorMessage.toString() : statusToString(status);

        response.setStatus(status);
        response.setCharacterEncoding("UTF-8");

        String acceptableHeader = request.getHeader("Accept");
        if (HttpHeadersUtil.isMimeTypeAccepted(acceptableHeader, APPLICATION_JSON_TYPE, DEFAULT_RESPONSE_TYPE)) {
            response.setContentType(APPLICATION_JSON_TYPE);
            JSON_WRITER.writeValue(response.getWriter(), errorMessage);
        } else {
            response.setContentType(DEFAULT_RESPONSE_TYPE);
            String html = getHtmlAsString(status, message);
            response.getWriter().print(html);
        }
    }

    private String getHtmlAsString(int status, String message) {
        String html = "";
        try (InputStream in = getClass().getResourceAsStream(HTML_FILE_PATH);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            html = reader.lines().collect(joining());
        } catch (IOException | NullPointerException e) {
            LOG.error("Can not read file " + HTML_FILE_PATH, e);
        }

        html = html.replace("{placeholder.status}", Integer.toString(status))
                .replace("{placeholder.message}", message);

        return html;
    }

    private static String statusToString(int statusCode) {
        switch (statusCode) {
            case 100:
                return "Continue";
            case 101:
                return "Switching Protocols";
            case 102:
                return "Processing (WebDAV)";
            case 200:
                return "OK";
            case 201:
                return "Created";
            case 202:
                return "Accepted";
            case 203:
                return "Non-Authoritative Information";
            case 204:
                return "No Content";
            case 205:
                return "Reset Content";
            case 206:
                return "Partial Content";
            case 207:
                return "Multi-Status (WebDAV)";
            case 300:
                return "Multiple Choices";
            case 301:
                return "Moved Permanently";
            case 302:
                return "Found";
            case 303:
                return "See Other";
            case 304:
                return "Not Modified";
            case 305:
                return "Use Proxy";
            case 307:
                return "Temporary Redirect";
            case 400:
                return "Bad Request";
            case 401:
                return "Unauthorized";
            case 402:
                return "Payment Required";
            case 403:
                return "Forbidden";
            case 404:
                return "Not Found";
            case 405:
                return "Method Not Allowed";
            case 406:
                return "Not Acceptable";
            case 407:
                return "Proxy Authentication Required";
            case 408:
                return "Request Time-out";
            case 409:
                return "Conflict";
            case 410:
                return "Gone";
            case 411:
                return "Length Required";
            case 412:
                return "Precondition Failed";
            case 413:
                return "Request Entity Too Large";
            case 414:
                return "Request-URI Too Large";
            case 415:
                return "Unsupported Media Type";
            case 416:
                return "Requested range not satisfiable";
            case 417:
                return "Expectation Failed";
            case 422:
                return "Unprocessable Entity (WebDAV)";
            case 423:
                return "Locked (WebDAV)";
            case 424:
                return "Failed Dependency (WebDAV)";
            case 500:
                return "Internal Server Error";
            case 501:
                return "Not Implemented";
            case 502:
                return "Bad Gateway";
            case 503:
                return "Service Unavailable";
            case 504:
                return "Gateway Time-out";
            case 505:
                return "HTTP Version not supported";
            case 507:
                return "Insufficient Storage (WebDAV)";
            case 510:
                return "Not Extended";
            default:
                return String.valueOf(statusCode);
        }
    }
}