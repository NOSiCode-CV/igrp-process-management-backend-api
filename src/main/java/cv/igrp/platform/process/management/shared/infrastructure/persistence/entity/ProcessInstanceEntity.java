/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.platform.process.management.shared.config.AuditEntity;
import cv.igrp.framework.stereotype.IgrpEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.util.UUID;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import java.util.ArrayList;
import java.util.List;

@Audited
@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_process_instance",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_t_process_instance_proc_release_id_number",
      columnNames = {
        "proc_release_id","number"
      }
    )
  })
public class ProcessInstanceEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;


    @NotBlank(message = "procReleaseKey is mandatory")
    @Column(name="proc_release_key", nullable = false)
    private String procReleaseKey;


    @NotBlank(message = "procReleaseId is mandatory")
    @Column(name="proc_release_id", nullable = false)
    private String procReleaseId;


    @Column(name="number")
    private String number;


    @Column(name="business_key")
    private String businessKey;


    @Column(name="version")
    private String version;


    @Column(name="started_at")
    private LocalDateTime startedAt;


    @Column(name="started_by")
    private String startedBy;


    @Column(name="ended_at")
    private LocalDateTime endedAt;


    @Column(name="ended_by")
    private String endedBy;


    @Column(name="canceled_at")
    private LocalDateTime canceledAt;


    @Column(name="canceled_by")
    private String canceledBy;


    @Column(name="obs_cancel")
    private String obsCancel;


    @Enumerated(EnumType.STRING)
    @Column(name="status")
    private ProcessInstanceStatus status;


    @NotBlank(message = "applicationBase is mandatory")
    @Column(name="application_base", nullable = false)
    private String applicationBase;


    @Column(name="name")
    private String name;


    @Column(name="engine_process_number")
    private String engineProcessNumber;


    @Column(name="priority")
    private Integer priority;


    @Column(name="is_archived")
    private Boolean isArchived;

     @OneToMany(mappedBy = "processInstanceId")
private List<TaskInstanceEntity> taskinstancelists = new ArrayList<>();


}
