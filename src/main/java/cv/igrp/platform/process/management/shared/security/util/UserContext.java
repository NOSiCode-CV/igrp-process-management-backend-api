package cv.igrp.platform.process.management.shared.security.util;

import cv.igrp.platform.process.management.shared.domain.models.Code;

public interface UserContext {

  Code getCurrentUser();

  String getCurrentUserName();
}
