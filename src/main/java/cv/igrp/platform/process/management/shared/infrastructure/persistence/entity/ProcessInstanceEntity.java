/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.framework.stereotype.IgrpEntity;
import cv.igrp.platform.process.management.shared.application.constants.ProcessInstanceStatus;
import cv.igrp.platform.process.management.shared.config.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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


    @Column(name="search_terms")
    private String searchTerms;


    @NotBlank(message = "applicationBase is mandatory")
    @Column(name="application_base", nullable = false)
    private String applicationBase;


    @Column(name="name")
    private String name;


    @Column(name="engine_process_number")
    private String engineProcessNumber;

     @OneToMany(mappedBy = "processInstanceId")
private List<TaskInstanceEntity> taskinstancelists;


}
