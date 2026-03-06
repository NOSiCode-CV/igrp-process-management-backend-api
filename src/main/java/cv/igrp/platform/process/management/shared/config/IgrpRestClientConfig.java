package cv.igrp.platform.process.management.shared.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Configuration responsible for creating the RestClient bean used by the
 * process management module. The bean is only instantiated when the
 * property `igrp.restclient.provider` is set to `igrp`.
 */
@Configuration
@ConditionalOnProperty(name = "igrp.restclient.provider", havingValue = "igrp")
public class IgrpRestClientConfig {

  @Bean
  public RestClient restClient(RestClient.Builder builder) {
    return builder.build();
  }
}
