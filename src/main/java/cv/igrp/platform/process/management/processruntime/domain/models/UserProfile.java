package cv.igrp.platform.process.management.processruntime.domain.models;

import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
public class UserProfile {

  private final Identifier id;
  private final String sub;
  private final String username;
  private String email;
  private Name firstName;
  private Name lastName;
  private Name fullName;

  @Builder
  public UserProfile(Identifier id,
                     String sub,
                     String username,
                     String email,
                     Name firstName,
                     Name lastName,
                     Name fullName
  ) {
    this.id = id == null ? Identifier.generate() : id;
    this.sub = Objects.requireNonNull(sub, "Username cannot be null!");
    this.username = Objects.requireNonNull(username, "Username cannot be null!");
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.fullName = resolveFullName(fullName, firstName, lastName);
  }

  private Name resolveFullName(Name fullName, Name firstName, Name lastName) {
    if (fullName != null) {
      return fullName;
    }
    StringBuilder sb = new StringBuilder();
    if (firstName != null && !firstName.getValue().isBlank()) {
      sb.append(firstName.getValue());
    }
    if (lastName != null && !lastName.getValue().isBlank()) {
      if (!sb.isEmpty()) sb.append(" ");
      sb.append(lastName.getValue());
    }
    return !sb.isEmpty() ? Name.create(sb.toString()) : null;
  }

}
