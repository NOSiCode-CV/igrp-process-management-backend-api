package cv.igrp.platform.process.management.processdefinition.domain.models;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BpmnXml {

  private final String xml;

  @Builder
  private BpmnXml(String xml) {
    if (xml == null || xml.isBlank()) {
      throw new IllegalArgumentException("The value of BpmnXml cannot be null or blank");
    }
    this.xml = xml;
  }

  public static BpmnXml create(String xml) {
    return new BpmnXml(xml);
  }

}
