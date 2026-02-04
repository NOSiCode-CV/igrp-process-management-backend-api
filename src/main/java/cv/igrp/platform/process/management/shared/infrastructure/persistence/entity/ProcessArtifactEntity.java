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

@Audited
@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_process_artifact",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_t_process_artifact_process_definition_id_key",
      columnNames = {
        "process_definition_id","key"
      }
    )
  })
public class ProcessArtifactEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

  
    @NotBlank(message = "name is mandatory")
    @Column(name="name", nullable = false)
    private String name;

  
    @NotBlank(message = "key is mandatory")
    @Column(name="key", nullable = false)
    private String key;

  
    @NotBlank(message = "processDefinitionId is mandatory")
    @Column(name="process_definition_id", nullable = false)
    private String processDefinitionId;

  
    @NotBlank(message = "formKey is mandatory")
    @Column(name="form_key", nullable = false)
    private String formKey;

  
    @Column(name="candidate_groups")
    private String candidateGroups;

  
    @Column(name="priority")
    private Integer priority;

  
    @Column(name="due_date")
    private LocalDateTime dueDate;

  
}