/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.platform.process.management.shared.config.AuditEntity;
import cv.igrp.framework.stereotype.IgrpEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.util.UUID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import cv.igrp.platform.process.management.shared.application.constants.Status;
import java.time.LocalDateTime;

@Audited
@Getter
@Setter
@ToString
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_area_process",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_t_area_process_area_id_proc_release_id",
      columnNames = {
        "proc_release_id","area_id"
      }
    )
  })
public class AreaProcessEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

  
    @NotNull(message = "areaId is mandatory")


  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", referencedColumnName = "id")
    private AreaEntity areaId;
    @NotBlank(message = "procReleaseKey is mandatory")
    @Column(name="proc_release_key", nullable = false)
    private String procReleaseKey;

  
    @NotBlank(message = "procReleaseId is mandatory")
    @Column(name="proc_release_id", nullable = false)
    private String procReleaseId;

  
    @NotNull(message = "status is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private Status status;

  
    @Column(name="version")
    private String version;

  
    @Column(name="removed_at")
    private LocalDateTime removedAt;

  
    @Column(name="removed_by")
    private String removedBy;

  
}