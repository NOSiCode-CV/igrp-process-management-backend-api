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
import jakarta.validation.constraints.NotNull;

@Audited
@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_task_priority",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_t_task_priority_code_process_definition_key",
      columnNames = {
        "code","process_definition_key"
      }
    )
  })
public class TaskPriorityEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;


    @NotBlank(message = "processDefinitionKey is mandatory")
    @Column(name="process_definition_key", nullable = false)
    private String processDefinitionKey;


    @NotBlank(message = "code is mandatory")
    @Column(name="code", nullable = false)
    private String code;


    @NotBlank(message = "label is mandatory")
    @Column(name="label", nullable = false)
    private String label;


    @NotNull(message = "weight is mandatory")
    @Column(name="weight", nullable = false)
    private Integer weight;


    @Column(name="color")
    private String color;


}
