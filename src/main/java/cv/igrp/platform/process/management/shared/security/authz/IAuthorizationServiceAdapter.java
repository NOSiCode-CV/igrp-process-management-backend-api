package cv.igrp.platform.process.management.shared.security.authz;

import java.util.Set;

public interface IAuthorizationServiceAdapter {

  Set<String> getRoles(String userIdentifier);
  Set<String> getPermissions(String userIdentifier);
  Set<String> getDepartments(String userIdentifier);

}
