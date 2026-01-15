package cv.igrp.platform.process.management.shared.security.authz;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Set;

public interface IAuthorizationServiceAdapter {

  Set<String> getRoles(String jwt, HttpServletRequest request);
  Set<String> getPermissions(String jwt, HttpServletRequest  request);
  Set<String> getDepartments(String jwt, HttpServletRequest  request);

}
