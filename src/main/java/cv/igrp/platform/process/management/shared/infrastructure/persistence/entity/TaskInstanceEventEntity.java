/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.framework.stereotype.IgrpEntity;
import cv.igrp.platform.process.management.shared.application.constants.TaskEventType;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.config.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.UUID;

@Audited
@Getter
@Setter
@ToString
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_task_instance_events")
public class TaskInstanceEventEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;


    @NotNull(message = "taskInstanceId is mandatory")


  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_instance_id", referencedColumnName = "id")
    private TaskInstanceEntity taskInstanceId;
    @NotNull(message = "eventType is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name="event_type", nullable = false)
    private TaskEventType eventType;


    @Column(name="started_at")
    private LocalDateTime startedAt;


    @Column(name="started_by", length=100)
    private String startedBy;


    @Column(name="input_task")
    private byte[] inputTask;


    @Column(name="output_task")
    private byte[] outputTask;


    @Column(name="start_obs", length=100)
    private String startObs;


    @Column(name="ended_at")
    private LocalDateTime endedAt;


    @Column(name="ended_by", length=100)
    private String endedBy;


    @Column(name="end_obs", length=400)
    private String endObs;


    @NotNull(message = "status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private TaskInstanceStatus status;


}
