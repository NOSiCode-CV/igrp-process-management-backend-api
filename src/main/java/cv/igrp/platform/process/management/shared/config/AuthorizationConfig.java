package cv.igrp.platform.process.management.shared.config;


import cv.igrp.platform.access.client.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthorizationConfig {

  @Bean
  public ApiClient igrpApiClient(@Value("${igrp.access.api.base-url}") String baseUrl) {
    ApiClient client = new ApiClient();
    client.setBaseUrl(baseUrl);
    return client;
  }

}
