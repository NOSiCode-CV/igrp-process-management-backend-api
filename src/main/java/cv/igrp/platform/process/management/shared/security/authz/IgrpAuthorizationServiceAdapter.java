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
          .map(this::normalizeRoleCode)
          .collect(Collectors.toSet());

    } catch (Exception e) {
      LOGGER.error("Error getting roles for current user", e);
      return Set.of();
    }

  }

  private String normalizeRoleCode(RoleDTO roleDTO) {
    if (roleDTO.getDepartmentCode() == null) {
      LOGGER.warn("Role {} has no department code", roleDTO.getCode());
      return roleDTO.getCode();
    }
    String prefix = roleDTO.getDepartmentCode() + ".";
    return roleDTO.getCode().startsWith(prefix)
        ? roleDTO.getCode()
        : prefix + roleDTO.getCode();
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

  @Override
  @Cacheable(value = "superAdminCache", key = "#jwt")
  public boolean isSuperAdmin(String jwt, HttpServletRequest request) {
    try{

      LOGGER.debug("Checking if current user is super admin");

      client.setAuthToken(jwt);
      var usersApi = new UsersApi(client);
      boolean isSuperAdmin = usersApi.isSuperadmin();

      LOGGER.debug("Current user is super admin: {}", isSuperAdmin);
      return isSuperAdmin;

    }catch (Exception e){
      LOGGER.error("Error checking if current user is super admin", e);
    }
    return false;
  }

}
