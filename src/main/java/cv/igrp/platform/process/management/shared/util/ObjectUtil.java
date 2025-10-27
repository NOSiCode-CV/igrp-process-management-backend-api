package cv.igrp.platform.process.management.shared.util;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

public class ObjectUtil {

  private static final Gson GSON = new GsonBuilder()
      .serializeNulls()
      .disableHtmlEscaping()
      .create();

  /**
   * Parses a JSON string into a JsonElement (handles objects, arrays, primitives, etc.).
   *
   * @param json the JSON string to parse
   * @return the parsed JsonElement, or JsonNull.INSTANCE if invalid or null/empty
   */
  public static JsonElement parseJsonStringObject(String json) {
    if (json == null || json.trim().isEmpty()) {
      return JsonNull.INSTANCE;
    }
    try {
      JsonElement element = JsonParser.parseString(json);
      return element != null ? element : JsonNull.INSTANCE;
    } catch (JsonSyntaxException e) {
      return JsonNull.INSTANCE;
    }
  }

  /**
   * Converts any Java object into a JsonElement.
   * - If the object is already a JsonElement, it returns it directly.
   * - If the object is a valid JSON string, it parses it.
   * - Otherwise, it serializes the object normally using Gson.
   *
   * @param obj the object to convert
   * @return the corresponding JsonElement, or JsonNull.INSTANCE if null or unconvertible
   */
  public static JsonElement parseJsonObject(Object obj) {
    switch (obj) {
      case null -> {
        return JsonNull.INSTANCE;
      }
      case JsonElement jsonElement -> {
        return jsonElement;
      }
      case String s -> {
        return parseJsonStringObject(s);
      }
      default -> {
      }
    }

    try {
      return GSON.toJsonTree(obj);
    } catch (Exception e) {
      return JsonNull.INSTANCE;
    }
  }

  public static Map<String, String> parseJsonObjectString(String json) {
    if (json == null || json.trim().isEmpty()) {
      return new HashMap<>();
    }
    try {
      Gson gson = new GsonBuilder().serializeNulls().create();
      Type type = new TypeToken<Map<String, String>>() {}.getType();
      Map<String, String> map = gson.fromJson(json, type);
      return map != null ? map : new HashMap<>();
    } catch (JsonSyntaxException e) {
      return new HashMap<>();
    }
  }

  public static String decodeBase64ToString(String base64) {
    if (base64 == null || base64.trim().isEmpty()) {
      return "";
    }
    try {
      byte[] decodedBytes =  Base64.getDecoder().decode(base64);
      return new String(decodedBytes, java.nio.charset.StandardCharsets.UTF_8);
    } catch (IllegalArgumentException e) {
      return "";
    }
  }

}
