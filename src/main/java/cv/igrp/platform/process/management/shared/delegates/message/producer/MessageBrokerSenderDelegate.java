package cv.igrp.platform.process.management.shared.delegates.message.producer;


import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.dto.ProcessMessageDTO;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("messageBrokerSenderDelegate")
@ConditionalOnBean(MessageBrokerSender.class)
public class MessageBrokerSenderDelegate implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageBrokerSenderDelegate.class);

  private final MessageBrokerSender messageSender;
  private final ObjectMapper objectMapper;
  private final ProcessInstanceService processInstanceService;
  private final TaskInstanceService taskInstanceService;

  public MessageBrokerSenderDelegate(MessageBrokerSender messageSender,
                                     ObjectMapper objectMapper,
                                     ProcessInstanceService processInstanceService,
                                     TaskInstanceService taskInstanceService) {
    this.messageSender = messageSender;
    this.objectMapper = objectMapper;
    this.processInstanceService = processInstanceService;
    this.taskInstanceService = taskInstanceService;
  }

  @Override
  public void execute(DelegateExecution execution) {
    LOGGER.info("Entered MessageSenderDelegate");

    String topic = execution.getVariable("topic", String.class);
    String businessKey = execution.getVariable("businessKey", String.class);

    if (topic == null || topic.isBlank()) {
      throw new IllegalArgumentException("'topic' which represent topic or queue is required.");
    }
    if (businessKey == null || businessKey.isBlank()) {
      throw new IllegalArgumentException("'businessKey' is required.");
    }
    messageSender.send(topic, createMessage(businessKey));
    LOGGER.info("Message successfully sent to: {}", topic);
  }

  private String createMessage(String businessKey) {

    ProcessInstance processInstance = processInstanceService.getProcessInstanceByBusinessKey(businessKey);

    ProcessMessageDTO processMessageDTO = new ProcessMessageDTO();
    processMessageDTO.setBusinessKey(businessKey);
    processMessageDTO.setProcessKey(processInstance.getProcReleaseKey().getValue());
    processMessageDTO.setProcessName(processInstance.getName());
    processMessageDTO.setVariables(processInstance.getVariables());
    processMessageDTO.setTimestamp(System.currentTimeMillis());

    PageableLista<TaskInstance> taskInstancePageableLista = taskInstanceService.getAllTaskInstances(
        TaskInstanceFilter.builder()
            .processInstanceId(processInstance.getId())
            .build()
    );

    taskInstancePageableLista.getContent().forEach(taskInstance -> {
      Map<String,Object> taskVariables = taskInstanceService.getTaskVariables(taskInstance.getId());
      ProcessMessageDTO.TaskMessageDTO taskMessageDTO = new ProcessMessageDTO.TaskMessageDTO();
      taskMessageDTO.setVariables(taskVariables);
      taskMessageDTO.setTaskKey(taskInstance.getTaskKey().getValue());
      taskMessageDTO.setTaskName(taskInstance.getName().getValue());
      processMessageDTO.getTasks().add(taskMessageDTO);
    });

    try {
      return objectMapper.writeValueAsString(processMessageDTO);
    } catch (Exception e) {
      LOGGER.error("Failed to serialize ProcessMessageDTO", e);
      throw new IllegalStateException("Error serializing message", e);
    }

  }

}
