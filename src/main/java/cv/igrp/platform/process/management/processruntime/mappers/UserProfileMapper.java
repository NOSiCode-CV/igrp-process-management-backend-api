package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.UserProfileDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.UserProfile;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.IAMUserProfileEntity;
import org.springframework.stereotype.Component;


@Component
public class UserProfileMapper {

  public UserProfileDTO toDTO(UserProfile model){
    if(model == null) return null;
    UserProfileDTO dto = new UserProfileDTO();
    dto.setId(model.getId().getValue());
    dto.setUsername(model.getUsername());
    dto.setSub(model.getSub());
    dto.setEmail(model.getEmail());
    dto.setFirstName(model.getFirstName() != null ? model.getFirstName().getValue() : null);
    dto.setLastName(model.getLastName() != null ? model.getLastName().getValue() : null);
    dto.setFullName(model.getFullName() != null ? model.getFullName().getValue() : null);
    return dto;
  }

  public IAMUserProfileEntity toEntity(UserProfile model){
    IAMUserProfileEntity entity = new IAMUserProfileEntity();
    entity.setUsername(model.getUsername());
    entity.setEmail(model.getEmail());
    entity.setFirstName(model.getFirstName() != null ? model.getFirstName().getValue() : null);
    entity.setLastName(model.getLastName() != null ? model.getLastName().getValue() : null);
    entity.setFullName(model.getFullName() != null ? model.getFullName().getValue() : null);
    entity.setId(model.getId().getValue());
    entity.setSub(model.getSub());
    return entity;
  }

  public UserProfile toModel(IAMUserProfileEntity entity){
    return UserProfile.builder()
        .id(Identifier.create(entity.getId()))
        .username(entity.getUsername())
        .email(entity.getEmail())
        .firstName(entity.getFirstName() != null ? Name.create(entity.getFirstName()) : null)
        .lastName(entity.getLastName() != null ? Name.create(entity.getLastName()) : null)
        .fullName(entity.getFullName() != null ? Name.create(entity.getFullName()) : null)
        .sub(entity.getSub())
        .build();
  }

}
