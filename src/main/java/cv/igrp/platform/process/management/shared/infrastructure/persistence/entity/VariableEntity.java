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
import cv.igrp.platform.process.management.shared.application.constants.VariableType;

@Audited
@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_variable")
public class VariableEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;

  
    @NotBlank(message = "name is mandatory")
    @Column(name="name", nullable = false)
    private String name;

  
    @NotNull(message = "type is mandatory")
    @Enumerated(EnumType.STRING)
    @Column(name="type", nullable = false)
    private VariableType type;

  


  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_instance_id", referencedColumnName = "id")
    private ProcessInstanceEntity processInstanceId;


  @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_instance_id", referencedColumnName = "id")
    private TaskInstanceEntity taskInstanceId;
    @Column(name="double_")
    private Double double;

  
    @Column(name="long_")
    private Long long;

  
    @Column(name="text_")
    private String text;

  
}