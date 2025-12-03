/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.platform.process.management.shared.config.AuditEntity;
import cv.igrp.framework.stereotype.IgrpEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.util.UUID;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import java.util.ArrayList;
import java.util.List;

@Audited
@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_task_instance")
public class TaskInstanceEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

  
    @Column(name="task_key", length=50)
    private String taskKey;

  
    @Column(name="external_id")
    private String externalId;

  
    @Column(name="form_key")
    private String formKey;

  
    @Column(name="name", length=100)
    private String name;

  
    @Column(name="candidate_groups")
    private String candidateGroups;

  


  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_instance_id", referencedColumnName = "id")
    private ProcessInstanceEntity processInstanceId;
    @Column(name="started_at")
    private LocalDateTime startedAt;

  
    @Column(name="started_by", length=100)
    private String startedBy;

  
    @Column(name="assigned_by", length=100)
    private String assignedBy;

  
    @Column(name="assigned_at")
    private LocalDateTime assignedAt;

  
    @Column(name="priority")
    private Integer priority;

  
    @Column(name="ended_at")
    private LocalDateTime endedAt;

  
    @Column(name="ended_by")
    private String endedBy;

  
    @NotNull(message = "status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private TaskInstanceStatus status;

     @OneToMany(mappedBy = "taskInstanceId")
private List<TaskInstanceEventEntity> taskinstanceevents = new ArrayList<>();


}