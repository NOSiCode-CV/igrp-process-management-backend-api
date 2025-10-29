package cv.igrp.platform.process.management.shared.delegates.message.producer;


import com.fasterxml.jackson.databind.ObjectMapper;
import cv.igrp.platform.process.management.processruntime.domain.models.ProcessInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstance;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceFilter;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import cv.igrp.platform.process.management.processruntime.domain.service.ProcessInstanceService;
import cv.igrp.platform.process.management.processruntime.domain.service.TaskInstanceService;
import cv.igrp.platform.process.management.shared.delegates.message.dto.ProcessMessageDTO;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("igrpMessageBrokerSenderDelegate")
@ConditionalOnBean(MessageBrokerSender.class)
public class MessageBrokerSenderDelegate implements JavaDelegate {

  private static final Logger LOGGER = LoggerFactory.getLogger(MessageBrokerSenderDelegate.class);

  private final MessageBrokerSender messageSender;
  private final ObjectMapper objectMapper;
  private final RuntimeProcessEngineRepository runtimeProcessEngineRepository;
  private final ProcessInstanceService processInstanceService;
  private final TaskInstanceService taskInstanceService;

  public Expression topic;

  public MessageBrokerSenderDelegate(MessageBrokerSender messageSender,
                                     ObjectMapper objectMapper,
                                     RuntimeProcessEngineRepository runtimeProcessEngineRepository,
                                     ProcessInstanceService processInstanceService,
                                     TaskInstanceService taskInstanceService) {
    this.messageSender = messageSender;
    this.objectMapper = objectMapper;
    this.runtimeProcessEngineRepository = runtimeProcessEngineRepository;
    this.processInstanceService = processInstanceService;
    this.taskInstanceService = taskInstanceService;
  }

  @Override
  public void execute(DelegateExecution execution) {
    LOGGER.info("Entered MessageSenderDelegate");

    if (topic == null) {
      throw new IllegalArgumentException("'topic' which represent topic or queue is required.");
    }

    String topicValue = topic.getValue(execution).toString();

    var message = createMessage(execution);

    LOGGER.info("Sending message...: \n{}\n", message);

    messageSender.send(topicValue, message);

    LOGGER.info("Message successfully sent to: {}", topicValue);

  }

  private String createMessage(DelegateExecution execution) {

    String businessKey = execution.getProcessInstanceBusinessKey();

    if (businessKey == null || businessKey.isBlank()) {
      throw new IllegalArgumentException("'businessKey' is required.");
    }

    ProcessMessageDTO processMessageDTO = new ProcessMessageDTO();

    try {

      ProcessInstance processInstance = processInstanceService.getProcessInstanceByBusinessKey(businessKey);

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
        Map<String, Object> taskVariables = taskInstanceService.getTaskVariables(taskInstance.getId());
        ProcessMessageDTO.TaskMessageDTO taskMessageDTO = new ProcessMessageDTO.TaskMessageDTO();
        taskMessageDTO.setVariables(taskVariables);
        taskMessageDTO.setTaskKey(taskInstance.getTaskKey().getValue());
        taskMessageDTO.setTaskName(taskInstance.getName().getValue());
        processMessageDTO.getTasks().add(taskMessageDTO);
      });

    } catch (IgrpResponseStatusException e) {

      String executionId = execution.getId();
      var runtime = execution.getEngineServices().getRuntimeService();
      var processDefinitionId = execution.getProcessDefinitionId();
      var processDefinition = runtimeProcessEngineRepository.getProcessDefinition(processDefinitionId);
      var processVariables = runtime.getVariables(executionId);

      processMessageDTO.setBusinessKey(businessKey);
      processMessageDTO.setProcessKey(processDefinition.key());
      processMessageDTO.setProcessName(processDefinition.name());
      processMessageDTO.setVariables(processVariables);
      processMessageDTO.setTimestamp(System.currentTimeMillis());

    }

    try {
      return objectMapper.writeValueAsString(processMessageDTO);
    } catch (Exception e) {
      LOGGER.error("Failed to serialize ProcessMessageDTO", e);
      throw new IllegalStateException("Error serializing message", e);
    }

  }

}
