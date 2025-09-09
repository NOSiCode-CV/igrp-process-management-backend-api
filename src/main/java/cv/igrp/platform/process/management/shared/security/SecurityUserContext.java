package cv.igrp.platform.process.management.shared.security;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUserContext implements UserContext {

  @Override
  public Code getCurrentUser() {
    return Code.create(getCurrentUserName());
  }

  @Override
  public String getCurrentUserName() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      return authentication.getName();
    }
    return null;
  }

}
