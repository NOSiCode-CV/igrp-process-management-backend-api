package cv.igrp.platform.process.management.shared.mapper;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import cv.igrp.platform.process.management.shared.util.ObjectUtil;
import org.activiti.engine.delegate.DelegateExecution;

public class ResponseVariableMapper {

  /**
   * Parses the JSON response and sets all primitive values (including nested ones)
   * as process variables in the execution context, using dot-notation for nested paths.
   * <p>
   * Example:
   * {
   *   "user": { "name": "John", "age": 30 },
   *   "roles": ["ADMIN", "USER"],
   *   "active": true
   * }
   * becomes:
   *  user.name = "John"
   *  user.age = 30
   *  roles_0 = "ADMIN"
   *  roles_1 = "USER"
   *  active = true
   */
  public static void mapAllPrimitivesToExecution(DelegateExecution execution, String responseBody) {
    JsonElement element = ObjectUtil.parseJsonStringObject(responseBody);
    if (element == null) return;

    if (element.isJsonObject()) {
      flattenAndSetVariables(execution, element.getAsJsonObject(), "");
    }
  }

  private static void flattenAndSetVariables(DelegateExecution execution, JsonObject jsonObject, String parentPath) {
    for (String key : jsonObject.keySet()) {
      JsonElement value = jsonObject.get(key);
      String path = parentPath.isEmpty() ? key : parentPath + "." + key;

      if (value.isJsonObject()) {
        flattenAndSetVariables(execution, value.getAsJsonObject(), path);
      } else if (value.isJsonArray()) {
        flattenArray(execution, value.getAsJsonArray(), path);
      } else if (value.isJsonPrimitive()) {
        setVariable(execution, path, value.getAsJsonPrimitive());
      }
    }
  }

  private static void flattenArray(DelegateExecution execution, JsonArray array, String parentPath) {
    for (int i = 0; i < array.size(); i++) {
      JsonElement item = array.get(i);
      String path = parentPath + "_" + i;

      if (item.isJsonObject()) {
        flattenAndSetVariables(execution, item.getAsJsonObject(), path);
      } else if (item.isJsonArray()) {
        flattenArray(execution, item.getAsJsonArray(), path);
      } else if (item.isJsonPrimitive()) {
        setVariable(execution, path, item.getAsJsonPrimitive());
      }
    }
  }

  private static void setVariable(DelegateExecution execution, String path, JsonPrimitive primitive) {
    Object value;

    if (primitive.isBoolean()) {
      value = primitive.getAsBoolean();
    } else if (primitive.isNumber()) {
      String numStr = primitive.getAsString();
      try {
        if (numStr.contains(".")) {
          value = Double.parseDouble(numStr);
        } else {
          value = Long.parseLong(numStr);
        }
      } catch (NumberFormatException e) {
        value = numStr;
      }
    } else {
      value = primitive.getAsString();
    }

    execution.setVariable(path, value);
  }
}
