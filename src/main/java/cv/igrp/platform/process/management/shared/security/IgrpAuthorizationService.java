package cv.igrp.platform.process.management.shared.security;

import cv.igrp.platform.access.client.ApiClient;
import cv.igrp.platform.access.client.api.AuthorizeApi;
import cv.igrp.platform.access.client.model.PermissionCheckRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("igrpAuthorization")
@SuppressWarnings("unused")
public class IgrpAuthorizationService {

  private static final Logger LOGGER = LoggerFactory.getLogger(IgrpAuthorizationService.class);

  private final ApiClient client;
  private final AuthenticationHelper authHelper;

  public IgrpAuthorizationService(ApiClient client, AuthenticationHelper authHelper) {
    this.client = client;
    this.authHelper = authHelper;
  }

  public boolean checkPermission(String resource, String permission) {
    try {

      LOGGER.debug("Checking permission for resource: {} and permission: {}", resource, permission);

      var token = authHelper.getToken();
      client.setAuthToken(token);

      var authorizeApi = new AuthorizeApi(client);

      var permissionCheckRequest = new PermissionCheckRequestDTO(resource, permission);

      var isAllowed = authorizeApi.checkAuthorization(permissionCheckRequest).isAllowed();

      LOGGER.debug("Permission check result: {}", isAllowed);

      return isAllowed;

    } catch (Exception e) {
      LOGGER.error("Error checking permission", e);
      throw new RuntimeException(e);
    }
  }
}

