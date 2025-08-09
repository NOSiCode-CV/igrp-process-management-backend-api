package cv.igrp.platform.process.management.processruntime.infrastructure.persistence.repository;

import cv.igrp.platform.process.management.IgrpPlatformProcessManagementApplication;
import cv.igrp.platform.process.management.processruntime.domain.repository.RuntimeProcessEngineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = IgrpPlatformProcessManagementApplication.class)
class RuntimeProcessEngineRepositoryImplIntegrationTest {

  @Autowired
  private RuntimeProcessEngineRepository runtimeProcessEngineRepository;

  @Test
  void startProcessInstanceById() {
  }

  @Test
  void getProcessInstanceById() {
    /*
    String processInstanceId = "07161346-738f-11f0-98fc-00090faa0001";
    ProcessInstance processInstance = runtimeProcessEngineRepository
        .getProcessInstanceById(processInstanceId);
    assertNotNull(processInstance);
    assertEquals(processInstanceId, processInstance.getNumber().getValue());
    */
  }

  @Test
  void getProcessInstanceTaskStatus() {
  }

  @Test
  void getActiveTaskInstances() {
  }

  @Test
  void completeTask() {
  }
}
