package cv.igrp.platform.process.management.shared.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

  @Bean
  @ConditionalOnMissingBean(RestClient.class) // Only if no other RestClient exists
  public RestClient defaultRestClient(RestClient.Builder builder) {
    return builder.build();
  }

}
