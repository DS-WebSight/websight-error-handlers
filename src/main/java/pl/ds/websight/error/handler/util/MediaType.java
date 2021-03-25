package pl.ds.websight.error.handler.util;

import java.util.HashMap;
import java.util.Map;

import static pl.ds.websight.error.handler.util.HttpHeadersUtil.QUALITY_VALUE;

public class MediaType {

    private final String value;
    private final String type;
    private final String subtype;
    private final float priority;
    private final Map<String, String> parameters;

    public MediaType(String mediaType) {
        this.value = mediaType;
        this.type = value.substring(0, value.indexOf('/'));
        this.subtype = prepareSubType();
        this.parameters = prepareParametersMap();
        this.priority = Float.valueOf(getParameters().get(QUALITY_VALUE));
    }

    public String getType() {
        return type;
    }

    public String getSubtype() {
        return subtype;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public float getPriority() {
        return priority;
    }

    private String prepareSubType() {
        int charIndex = value.indexOf(';');
        return value.substring(value.indexOf('/') + 1, charIndex == -1 ? value.length() : charIndex);
    }

    private Map<String, String> prepareParametersMap() {
        Map<String, String> parametersMap = new HashMap<>();

        int charIndex = value.indexOf(';');
        if (charIndex != -1) {
            String parametersString = value.substring(charIndex + 1);
            String[] parameters = parametersString.split(";");

            for (String parameter : parameters) {
                int signIndex = parameter.indexOf('=');
                if (signIndex != -1) {
                    String parameterKey = parameter.substring(0, signIndex);
                    String parameterValue = parameter.substring(signIndex + 1);
                    parametersMap.put(parameterKey, parameterValue);
                }
            }
        }
        setDefaultValuesForParameters(parametersMap);
        return parametersMap;
    }

    private void setDefaultValuesForParameters(Map<String, String> parametersMap) {
        if (!parametersMap.containsKey(QUALITY_VALUE)) {
            parametersMap.put(QUALITY_VALUE, "1");
        }
    }
}
