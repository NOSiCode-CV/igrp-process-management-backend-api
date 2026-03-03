package cv.igrp.platform.process.management.shared.security.util;

import cv.igrp.platform.process.management.shared.domain.models.Code;

import java.util.List;

public interface UserContext {

  Code getCurrentUser();

  String getCurrentUserName();

  List<String> getCurrentGroups();

  List<String> getCurrentRoles();

  boolean isSuperAdmin();

}
