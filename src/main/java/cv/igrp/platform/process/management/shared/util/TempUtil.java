package cv.igrp.platform.process.management.shared.util;

import cv.igrp.platform.process.management.shared.domain.models.Code;

public class TempUtil { // todo class to be removed
  public static Code getCurrentUser(){
    return Code.create("demo@nosi.cv");
  }
}
