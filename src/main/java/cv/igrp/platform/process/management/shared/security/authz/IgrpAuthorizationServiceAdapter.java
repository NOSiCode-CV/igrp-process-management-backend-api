package cv.igrp.platform.process.management.shared.security.authz;

import cv.igrp.platform.access.client.ApiClient;
import cv.igrp.platform.access.client.api.UsersApi;
import cv.igrp.platform.access.client.model.DepartmentDTO;
import cv.igrp.platform.access.client.model.PermissionDTO;
import cv.igrp.platform.access.client.model.RoleDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.Cacheable;


@Component
@ConditionalOnProperty(
    name = "igrp.authorization.service.adapter",
    havingValue = "igrp"
)
public class IgrpAuthorizationServiceAdapter implements IAuthorizationServiceAdapter {

  private static final Logger LOGGER = LoggerFactory.getLogger(IgrpAuthorizationServiceAdapter.class);

  private final ApiClient client;

  public IgrpAuthorizationServiceAdapter(ApiClient client) {
    this.client = client;
  }

  @Override
  @Cacheable(value = "rolesCache", key = "#jwt",  unless = "#result.isEmpty()")
  public Set<String> getRoles(String jwt, HttpServletRequest request) {
    try {

      LOGGER.debug("Getting roles for current user");

      client.setAuthToken(jwt);

      var usersApi = new UsersApi(client);

      List<RoleDTO> roles = usersApi.getCurrentUserRoles();
      LOGGER.debug("Roles: {}", roles);

      return roles.stream()
          .map(RoleDTO::getName)
          .collect(Collectors.toSet());

    } catch (Exception e) {
      LOGGER.error("Error getting roles for current user", e);
      return Set.of();
    }

  }

  @Override
  @Cacheable(value = "permissionsCache", key = "#jwt",  unless = "#result.isEmpty()")
  public Set<String> getPermissions(String jwt, HttpServletRequest request) {
    try{

      LOGGER.debug("Getting permissions for current user");

      client.setAuthToken(jwt);

      var usersApi = new UsersApi(client);

      List<PermissionDTO> permissions = usersApi.getCurrentUserPermissions(null);
      LOGGER.debug("Permissions: {}", permissions);

      return permissions.stream()
          .map(PermissionDTO::getName)
          .collect(Collectors.toSet());

    }catch (Exception e){
      LOGGER.error("Error getting permissions for current user", e);
      return Set.of();
    }

  }

  @Override
  @Cacheable(value = "departmentsCache", key = "#jwt", unless = "#result.isEmpty()")
  public Set<String> getDepartments(String jwt, HttpServletRequest request) {
    try {

      LOGGER.debug("Getting departments for current user");

      client.setAuthToken(jwt);

      var usersApi = new UsersApi(client);

      List<DepartmentDTO> departments = usersApi.getCurrentUserDepartments();
      LOGGER.debug("Departments: {}", departments);

      return departments.stream()
          .map(DepartmentDTO::getCode)
          .collect(Collectors.toSet());

    } catch (Exception e) {
      LOGGER.error("Error getting departments for current user", e);
      return Set.of();
    }

  }

}
