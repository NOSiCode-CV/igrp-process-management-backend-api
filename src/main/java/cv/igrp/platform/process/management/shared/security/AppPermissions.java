package cv.igrp.platform.process.management.shared.security;

import cv.igrp.framework.stereotype.IgrpPermission;


public class AppPermissions {

  @IgrpPermission(name = "igrp.process.view.deployment", description = "Permission to view process deployments")
  public static final String IGRP_PROCESS_VIEW_DEPLOYMENT = "igrp.process.view.deployment";

}
