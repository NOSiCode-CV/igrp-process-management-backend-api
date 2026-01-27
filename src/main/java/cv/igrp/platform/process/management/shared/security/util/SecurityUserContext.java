package cv.igrp.platform.process.management.shared.security.util;

import cv.igrp.platform.process.management.shared.domain.models.Code;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

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

  @Override
  public List<String> getCurrentGroups() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication == null || !authentication.isAuthenticated()) {
      return List.of();
    }

    return authentication.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .filter(role -> role.startsWith(ActivitiConstants.GROUP_PREFIX))
        .map(role -> role.substring(ActivitiConstants.GROUP_PREFIX.length()))
        .toList();
  }

}
