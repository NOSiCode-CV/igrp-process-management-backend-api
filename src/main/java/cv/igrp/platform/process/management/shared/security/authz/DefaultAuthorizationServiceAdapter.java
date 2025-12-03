package cv.igrp.platform.process.management.shared.security.authz;

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


  @Override
  public Set<String> getRoles(String userIdentifier) {
    return Set.of();
  }

  @Override
  public Set<String> getPermissions(String userIdentifier) {
    return Set.of();
  }

  @Override
  public Set<String> getDepartments(String userIdentifier) {
    return Set.of();
  }

}
