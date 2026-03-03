package cv.igrp.platform.process.management.shared.events;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TimerEventListener implements ActivitiEventListener {

  private static final Logger log = LoggerFactory.getLogger(TimerEventListener.class);

  @Override
  public void onEvent(ActivitiEvent event) {

    log.info("Event received: {}", event.getType());

    if (event.getType() == ActivitiEventType.TIMER_FIRED) {

      log.info("🔥 Timer fired on process instance {}",
          event.getProcessInstanceId());

      // Example: list current active activities
      // (This is what you asked)
    }

  }

  @Override
  public boolean isFailOnException() {
    return false;
  }

}
