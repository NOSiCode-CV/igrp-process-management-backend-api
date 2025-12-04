package cv.igrp.platform.process.management.shared.delegates.webhook;

import cv.igrp.platform.process.management.shared.util.MessageUtil;
import cv.igrp.platform.process.management.shared.util.ObjectUtil;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Objects;

import static java.util.Optional.ofNullable;

@Component("igrpProcessWebhookDelegate")
public class IgrpProcessWebhookDelegate implements JavaDelegate {

  private final MessageUtil messageUtil;

  private static final Logger log = LoggerFactory.getLogger(IgrpProcessWebhookDelegate.class);

  private final RestClient restClient = RestClient.create();

  @Value(value = "${igrp.delegate.webhook.auth-token:}")
  private String globalAuthToken;

  public Expression webhookUrl;
  public Expression webhookUrlPath;
  public Expression webhookPayloadHeader;

  public IgrpProcessWebhookDelegate(MessageUtil messageUtil) {
    this.messageUtil = messageUtil;
  }

  @Override
  public void execute(DelegateExecution execution) {

    String taskId = execution.getCurrentActivityId();
    String processInstanceId = execution.getProcessInstanceId();
    log.info("[IgrpProcessWebhookDelegate] Executing webhook task: {} from process instance: {}", taskId, processInstanceId);
    String baseUrlVariable = (String) execution.getVariable("webhookUrl");
    String baseUrl = Objects.nonNull(baseUrlVariable)? baseUrlVariable: Objects.nonNull(webhookUrl)? webhookUrl.getValue(execution).toString() : null;
    String pathVariable = (String)  execution.getVariable("webhookUrlPath");
    String path = Objects.nonNull(pathVariable) ?  pathVariable : Objects.nonNull(webhookUrlPath) ? (String) webhookUrlPath.getValue(execution): null;

    String url = UriComponentsBuilder.fromUriString(baseUrl)
        .path(path != null ? path : "")
        .build()
        .toUriString();

    String payload = messageUtil.createMessage(execution);

    Object payloadHeader = execution.getVariable("webhookPayloadHeader");
    Map<String, String> headersMap = ObjectUtil.parseJsonObjectString(
        ofNullable(Objects.nonNull(payloadHeader)? payloadHeader : Objects.nonNull(webhookPayloadHeader) ? webhookPayloadHeader.getValue(execution) : null)
            .orElse("").toString()
    );

    try {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      if (!headersMap.isEmpty()) {
        headersMap.forEach(headers::set);
      } else {
        headers.set("Authorization", "Bearer " + globalAuthToken);
      }

      log.info("[IgrpProcessWebhookDelegate] Sending request to {}", url);
      log.debug("[IgrpProcessWebhookDelegate] Payload: {}", payload);

      restClient.post()
          .uri(url)
          .headers(httpHeaders -> httpHeaders.addAll(headers))
          .body(payload)
          .retrieve()
          .toEntity(String.class);

      log.info("[IgrpProcessWebhookDelegate] Process Data successfully sent to webhook");

    } catch (RestClientResponseException e) {
      log.warn("[IgrpProcessWebhookDelegate] Webhook returned error {}: {}", e.getStatusCode(), e.getMessage());
    } catch (Exception e) {
      log.error("[IgrpProcessWebhookDelegate] Error calling webhook {}", url, e);
    }
  }

}
