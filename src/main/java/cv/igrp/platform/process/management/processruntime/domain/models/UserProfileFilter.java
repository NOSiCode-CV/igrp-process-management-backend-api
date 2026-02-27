package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserProfileFilter {

  private final Integer page;
  private final Integer size;
  private final String sub;
  private final String username;
  private final String email;
  private final Name name;

  @Builder
  public UserProfileFilter(Integer page,
                           Integer size,
                           String sub,
                           String username,
                           String email,
                           Name name
  ) {
    this.page = page == null ? 0 : page;
    this.size = size == null ? 50 : size;
    this.sub = sub;
    this.username = username;
    this.email = email;
    this.name = name;
  }

}
