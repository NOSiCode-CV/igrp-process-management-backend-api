package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.processruntime.domain.models.UserProfile;
import cv.igrp.platform.process.management.processruntime.domain.models.UserProfileFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.UserProfileRepository;
import cv.igrp.platform.process.management.processruntime.mappers.UserProfileMapper;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.repository.IAMUserProfileEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public class IAMUserProfileRepositoryImpl implements UserProfileRepository {

  private final IAMUserProfileEntityRepository repository;
  private final UserProfileMapper mapper;

  public IAMUserProfileRepositoryImpl(IAMUserProfileEntityRepository repository,
                                      UserProfileMapper mapper
  ) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public UserProfile save(UserProfile userProfile) {
    return mapper.toModel(
        repository.save(mapper.toEntity(userProfile))
    );
  }

  @Override
  public PageableLista<UserProfile> findAll(UserProfileFilter filter) {
    return null;
  }

  @Override
  public Optional<UserProfile> findBySubject(String id) {
    return repository.findBySub(id).map(mapper::toModel);
  }

  @Override
  public List<UserProfile> findBySubject(Set<String> ids) {
    return repository.findBySubIn(ids).stream().map(mapper::toModel).toList();
  }

}
