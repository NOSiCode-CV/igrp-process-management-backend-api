package cv.igrp.platform.process.management.processruntime.mappers;

import cv.igrp.platform.process.management.processruntime.application.dto.TaskInstanceEventListDTO;
import cv.igrp.platform.process.management.processruntime.domain.models.TaskInstanceEvent;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEntity;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.TaskInstanceEventEntity;
import org.springframework.stereotype.Component;

@Component
public class TaskInstanceEventMapper {

  public TaskInstanceEventEntity toEventEntity(TaskInstanceEvent taskInstanceEvent) {
      var eventEntity = new TaskInstanceEventEntity();
      eventEntity.setId(taskInstanceEvent.getId().getValue());
      eventEntity.setEventType(taskInstanceEvent.getEventType());
      eventEntity.setStatus(taskInstanceEvent.getStatus());
      eventEntity.setPerformedAt(taskInstanceEvent.getPerformedAt());
      eventEntity.setPerformedBy(taskInstanceEvent.getPerformedBy().getValue());
      eventEntity.setNote(taskInstanceEvent.getNote());
      if (taskInstanceEvent.getTaskInstanceId() != null) {
            var taskInstanceEntity = new TaskInstanceEntity();
            taskInstanceEntity.setId(taskInstanceEvent.getTaskInstanceId().getValue());
            eventEntity.setTaskInstanceId(taskInstanceEntity);
      }
      return eventEntity;
  }


  public TaskInstanceEvent toEventModel(TaskInstanceEventEntity eventEntity) {
      return TaskInstanceEvent.builder()
          .id(Identifier.create(eventEntity.getId()))
          .taskInstanceId(Identifier.create(eventEntity.getTaskInstanceId().getId()))
          .eventType(eventEntity.getEventType())
          .status(eventEntity.getStatus())
          .performedAt(eventEntity.getPerformedAt())
          .performedBy(Code.create(eventEntity.getPerformedBy()))
          .note(eventEntity.getNote())
          .build();
  }


  public TaskInstanceEventListDTO toEventListDTO(TaskInstanceEvent event) {
      var eventDto = new TaskInstanceEventListDTO();
      eventDto.setId(event.getId().getValue());
      eventDto.setTaskInstanceId(event.getTaskInstanceId().getValue());
      eventDto.setEventType(event.getEventType().getCode());
      eventDto.setPerformedAt(event.getPerformedAt());
      eventDto.setPerformedBy(event.getPerformedBy().getValue());
      eventDto.setObs(event.getNote());
      eventDto.setStatus(event.getStatus());
      return eventDto;
  }

}
