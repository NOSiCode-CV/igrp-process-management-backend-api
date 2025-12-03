package cv.igrp.platform.process.management.shared.config;


import cv.igrp.platform.access.client.ApiClient;
import cv.igrp.platform.process.management.shared.security.util.AuthenticationHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationConfig {

  @Bean
  public ApiClient igrpApiClient(@Value("${igrp.access.api.base-url}") String baseUrl, AuthenticationHelper authenticationHelper) {
    ApiClient client = new ApiClient();
    client.setBaseUrl(baseUrl);
    return client;
  }

}
