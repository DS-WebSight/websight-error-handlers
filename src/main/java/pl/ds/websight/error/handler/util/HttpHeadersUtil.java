package pl.ds.websight.error.handler.util;

import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class HttpHeadersUtil {

    protected static final String QUALITY_VALUE = "q";
    private static final String ANY_TYPE = "*";

    private HttpHeadersUtil() {
        // No instances
    }

    /**
     * Method is checking if given MIME Type is accepted in comparison to default MIME Type.
     *
     * @param acceptableHeaderString, String with Accept Header,
     * @param mimeType, MIME Type that we want to check if is accepted,
     * @param defaultMimeType, MIME Type which will be compared with 'mimeType',
     * @return true if type is accepted, otherwise false.
     */
    public static boolean isMimeTypeAccepted(String acceptableHeaderString, String mimeType, String defaultMimeType) {
        if (acceptableHeaderString == null || acceptableHeaderString.isEmpty() || mimeType == null || defaultMimeType == null) {
            return false;
        }
        MediaType searchingMediaType = new MediaType(mimeType);
        MediaType searchingDefaultMediaType = new MediaType(defaultMimeType);

        List<MediaType> acceptableMediaTypes = HttpHeadersUtil.getAcceptableMediaTypes(acceptableHeaderString);
        if (!HttpHeadersUtil.hasAcceptableMediaType(acceptableMediaTypes, searchingMediaType.getType(), searchingMediaType.getSubtype())) {
            return false;
        }
        if (!HttpHeadersUtil.hasAcceptableMediaType(acceptableMediaTypes, searchingDefaultMediaType.getType(), searchingDefaultMediaType.getSubtype())) {
            return true;
        }

        MediaType mediaType = HttpHeadersUtil.getMediaType(acceptableMediaTypes, searchingMediaType.getType(), searchingMediaType.getSubtype());
        MediaType defaultMediaType = HttpHeadersUtil.getMediaType(acceptableMediaTypes, searchingDefaultMediaType.getType(),
                searchingDefaultMediaType.getSubtype());
        if (mediaType == null || defaultMediaType == null) {
            return false;
        }

        float mediaTypePriority = mediaType.getPriority();
        float defaultMediaTypePriority = defaultMediaType.getPriority();

        if (Float.compare(mediaTypePriority, defaultMediaTypePriority) != 0) {
            return mediaTypePriority > defaultMediaTypePriority;
        }

        return isMediaTypeMoreSpecific(mediaType, defaultMediaType);
    }

    private static boolean isMediaTypeMoreSpecific(MediaType mediaType, MediaType defaultMediaType) {
        String mediaTypeType = mediaType.getType();
        String mediaTypeSubtype = mediaType.getSubtype();
        String defaultMediaTypeType = defaultMediaType.getType();
        String defaultMediaTypeSubtype = defaultMediaType.getSubtype();

        if (!mediaTypeType.equals(ANY_TYPE) && defaultMediaTypeType.equals(ANY_TYPE)) {
            return true;
        }
        if (mediaTypeType.equals(ANY_TYPE) && defaultMediaTypeType.equals(ANY_TYPE)) {
            return !mediaTypeSubtype.equals(ANY_TYPE);
        }
        if (!mediaTypeType.equals(ANY_TYPE)) {
            return !mediaTypeSubtype.equals(ANY_TYPE) && defaultMediaTypeSubtype.equals(ANY_TYPE);
        }
        return false;
    }

    private static List<MediaType> getAcceptableMediaTypes(String acceptableHeader) {
        String[] acceptableHeaders = acceptableHeader.split(",");

        return Stream.of(acceptableHeaders).map(MediaType::new).collect(toList());
    }

    private static boolean hasAcceptableMediaType(List<MediaType> mediaTypes, String acceptableType, String acceptableSubtype) {
        for (MediaType mediaType : mediaTypes) {
            if ((mediaType.getType().equals(acceptableType) || mediaType.getType().equals(ANY_TYPE)) &&
                    (mediaType.getSubtype().equals(acceptableSubtype) || mediaType.getSubtype().equals(ANY_TYPE))) {
                return true;
            }
        }
        return false;
    }

    private static MediaType getAcceptableMediaType(List<MediaType> mediaTypes, String acceptableType, String acceptableSubtype) {
        for (MediaType mediaType : mediaTypes) {
            if (mediaType.getType().equals(acceptableType) &&
                    (mediaType.getSubtype().equals(acceptableSubtype) || mediaType.getSubtype().equals(ANY_TYPE))) {
                return mediaType;
            }
        }
        return null;
    }

    private static MediaType getMediaType(List<MediaType> acceptableMediaTypes, String mimeType, String mimeSubtype) {
        MediaType mediaType = HttpHeadersUtil.getAcceptableMediaType(acceptableMediaTypes, mimeType, mimeSubtype);
        if (mediaType == null) {
            mediaType = HttpHeadersUtil.getAcceptableMediaType(acceptableMediaTypes, mimeType, ANY_TYPE);
        }
        if (mediaType == null) {
            mediaType = HttpHeadersUtil.getAcceptableMediaType(acceptableMediaTypes, ANY_TYPE, ANY_TYPE);
        }
        return mediaType;
    }
}
