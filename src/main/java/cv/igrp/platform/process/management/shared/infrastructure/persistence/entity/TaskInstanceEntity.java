/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.framework.stereotype.IgrpEntity;
import cv.igrp.platform.process.management.shared.application.constants.TaskInstanceStatus;
import cv.igrp.platform.process.management.shared.config.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Audited
@Getter
@Setter
@ToString
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_task_instance")
public class TaskInstanceEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;




  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_instance_id", referencedColumnName = "id")
    private ProcessInstanceEntity processInstanceId;
    @Column(name="task_key", length=50)
    private String taskKey;


    @Column(name="name", length=100)
    private String name;


    @Column(name="external_id")
    private String externalId;


    @Column(name="started_at")
    private LocalDateTime startedAt;


    @Column(name="started_by", length=100)
    private String startedBy;


    @Column(name="assigned_by", length=100)
    private String assignedBy;


    @Column(name="assigned_at")
    private LocalDateTime assignedAt;


    @NotNull(message = "status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private TaskInstanceStatus status;


    @NotBlank(message = "applicationBase is mandatory")
    @Column(name="application_base", nullable = false)
    private String applicationBase;


    @Column(name="search_terms")
    private String searchTerms;

     @OneToMany(mappedBy = "taskInstanceId")
private List<TaskInstanceEventEntity> taskinstanceevents;


}
