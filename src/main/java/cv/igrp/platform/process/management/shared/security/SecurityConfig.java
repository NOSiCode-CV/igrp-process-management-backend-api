package cv.igrp.platform.process.management.shared.security;


import cv.igrp.platform.process.management.shared.security.authz.IAuthorizationServiceAdapter;
import cv.igrp.platform.process.management.shared.security.util.ActivitiConstants;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.TokenExchangeOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.cors.CorsConfiguration;

import java.util.HashSet;
import java.util.Set;

import static cv.igrp.platform.process.management.shared.security.util.IgrpAuthorizationConstants.ROLE_PREFIX;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

  private final IAuthorizationServiceAdapter authorizationService;

  public SecurityConfig(IAuthorizationServiceAdapter authorizationService) {
    this.authorizationService = authorizationService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.cors(cors -> cors.configurationSource(request -> {
      var configuration = new CorsConfiguration();
      configuration.addAllowedOriginPattern(CorsConfiguration.ALL);
      configuration.addAllowedMethod(HttpMethod.GET);
      configuration.addAllowedMethod(HttpMethod.POST);
      configuration.addAllowedMethod(HttpMethod.PUT);
      configuration.addAllowedMethod(HttpMethod.PATCH);
      configuration.addAllowedMethod(HttpMethod.DELETE);
      configuration.addAllowedMethod(HttpMethod.HEAD);
      configuration.addAllowedMethod(HttpMethod.OPTIONS);
      configuration.addAllowedHeader(CorsConfiguration.ALL);
      configuration.setAllowCredentials(true);
      return configuration;
    }));

    // Configure OAuth2 Resource Server to use JWT tokens for authentication
    http.oauth2ResourceServer((oauth2ResourceServer) -> oauth2ResourceServer
        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
    );

    // Configure authorization rules and policy enforcement
    http
        .authorizeHttpRequests((authorize) -> authorize
            .requestMatchers(
                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                "/swagger-resources/**", "/webjars/**", "/actuator/**"
            ).permitAll()
            .anyRequest()
            .authenticated()  // Require authentication for all other requests
        )
        .exceptionHandling(ex -> ex.authenticationEntryPoint((request, response, authException) -> {
          response.addHeader(HttpHeaders.WWW_AUTHENTICATE, "Basic realm=\"Restricted Content\"");
          response.sendError(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());
        }));

    // Set session management to stateless (no session created for API requests)
    http.sessionManagement(t -> t.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    // Disable CSRF
    http.csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {

    var converter = new JwtAuthenticationConverter();

    converter.setJwtGrantedAuthoritiesConverter(jwt -> {

      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder
              .getRequestAttributes())
              .getRequest();

      Set<GrantedAuthority> authorities = new HashSet<>();
      final String token = jwt.getTokenValue();
      authorizationService
          .getRoles(token, request)
          .forEach(r -> {
            String roleValue = !r.startsWith(ROLE_PREFIX) ?  ROLE_PREFIX + r : r;
            String groupValue = !r.startsWith(ActivitiConstants.GROUP_PREFIX) ? ActivitiConstants.GROUP_PREFIX + r : r;
            authorities.add(new SimpleGrantedAuthority(roleValue));
            authorities.add(new SimpleGrantedAuthority(groupValue));
          });

      authorizationService
          .getPermissions(token, request)
          .forEach(p -> {
            authorities.add(new SimpleGrantedAuthority(p));
          });

      authorizationService
          .getDepartments(token, request)
          .forEach(d -> {
            authorities.add(new SimpleGrantedAuthority(d));
            String groupValue = !d.startsWith(ActivitiConstants.GROUP_PREFIX) ? ActivitiConstants.GROUP_PREFIX + d : d;
            authorities.add(new SimpleGrantedAuthority(groupValue));
          });

      //authorizationService.getActiveRoles(token, request);

      // Activiti Admin or User role
      if(authorizationService.isSuperAdmin(token, request)){
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + ActivitiConstants.ROLE_ACTIVITI_ADMIN));
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + ActivitiConstants.ROLE_ACTIVITI_USER));
      } else {
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + ActivitiConstants.ROLE_ACTIVITI_USER));
      }

      LOGGER.debug("Authorities: {}", authorities);

      return authorities;

    });

    return converter;
  }

  @Bean
  public OAuth2AuthorizedClientProvider tokenExchange() {
    return new TokenExchangeOAuth2AuthorizedClientProvider();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return username -> {
      throw new UsernameNotFoundException("Hi, i am a dummy. UserDetailsService not used with JWT/Keycloak");
    };
  }

}
