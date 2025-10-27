package cv.igrp.platform.process.management.shared.delegates.parse;

import cv.igrp.platform.process.management.shared.mapper.ResponseVariableMapper;
import cv.igrp.platform.process.management.shared.util.ObjectUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component("igrpJsonParseDelegate")
public class IgrpJsonParseDelegate implements JavaDelegate {

  private static final Logger log = LoggerFactory.getLogger(IgrpJsonParseDelegate.class);

  public Expression json;
  public Expression isBase64Encoded;

  @Override
  public void execute(DelegateExecution execution) {

    String jsonVariable = (String) execution.getVariable("json");
    String payload = Objects.nonNull(jsonVariable)? jsonVariable: Objects.nonNull(json)? json.getValue(execution).toString() : null;
    String isBase64Variable = (String) execution.getVariable("isBase64Encoded");
    boolean isBase64 = Objects.nonNull(isBase64Variable) ? Boolean.parseBoolean(isBase64Variable) : Objects.nonNull(isBase64Encoded) ? Boolean.parseBoolean((String) isBase64Encoded.getValue(execution)) : Boolean.FALSE;

    if (isBase64) {
      payload = ObjectUtil.decodeBase64ToString(payload);
    }

    try {

      ResponseVariableMapper.mapAllPrimitivesToExecution(execution, payload);

      log.info("[IgrpJsonParseDelegate] Data parsed successfully");

    } catch (Exception e) {
      log.error("[IgrpJsonParseDelegate] Error parsing JSON: ", e);
      execution.setTransientVariable("jsonParseError", e.getMessage());
    }

  }

}
