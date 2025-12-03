package cv.igrp.platform.process.management.shared.security.authz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ConditionalOnProperty(
    name = "igrp.authorization.service.adapter",
    havingValue = "default",
    matchIfMissing = true
)
public class DefaultAuthorizationServiceAdapter implements IAuthorizationServiceAdapter{

  private static final Logger log = LoggerFactory.getLogger(DefaultAuthorizationServiceAdapter.class);

  @Override
  public Set<String> getRoles(String userIdentifier) {
    log.debug("Fetching roles for user: {}", userIdentifier);
    return Set.of();
  }

  @Override
  public Set<String> getPermissions(String userIdentifier) {
    log.debug("Fetching permissions for user: {}", userIdentifier);
    return Set.of();
  }

  @Override
  public Set<String> getDepartments(String userIdentifier) {
    log.debug("Fetching departments for user: {}", userIdentifier);
    return Set.of();
  }

}
