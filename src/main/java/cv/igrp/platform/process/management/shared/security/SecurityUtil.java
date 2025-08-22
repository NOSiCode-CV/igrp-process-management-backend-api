package cv.igrp.platform.process.management.shared.security;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    private SecurityUtil(){}

    public static Code getCurrentUser() {
      return Code.create(getCurrentUserName());
    }

    public static String getCurrentUserName() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null && authentication.isAuthenticated()) {
        return authentication.getName();
      }
      return null;
    }
}
