package com.sa.shared.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Optional;

public final class JsonUtils {
    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final int BUFFER_SIZE = 4096;

    static {
        OBJECT_MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        OBJECT_MAPPER.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
        OBJECT_MAPPER.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        OBJECT_MAPPER.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    private JsonUtils() {
        // default
    }

    public static <T> String toJson(T source) {
        if (source == null)
            return null;
        try {
            return OBJECT_MAPPER.writeValueAsString(source);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static <T> Optional<T> fromJson(InputStream stream, Class<T> type) {
        try {
            if (stream == null || stream.available() == 0)
                return Optional.empty();
            return Optional.of(fromJson(copyToString(stream), type));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }


    public static <T> T fromJson(String json, Class<T> type) {
        if (type == null || StringUtils.isEmpty(json))
            return null;
        try {
            return OBJECT_MAPPER.readValue(json, type);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    static String copyToString(InputStream stream) throws IOException {
        StringBuilder out = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(stream, Charset.defaultCharset());
        char[] buffer = new char[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = reader.read(buffer)) != -1) {
            out.append(buffer, 0, bytesRead);
        }
        return out.toString();
    }
}
