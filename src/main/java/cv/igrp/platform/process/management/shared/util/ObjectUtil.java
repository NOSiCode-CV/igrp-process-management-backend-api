package cv.igrp.platform.process.management.shared.util;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class ObjectUtil {

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

}
