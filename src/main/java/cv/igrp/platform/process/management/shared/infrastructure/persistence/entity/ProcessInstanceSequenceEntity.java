/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.shared.infrastructure.persistence.entity;

import cv.igrp.framework.stereotype.IgrpEntity;
import cv.igrp.platform.process.management.shared.config.AuditEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_process_instance_sequence",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "sequence_uk_processdef_application",
      columnNames = {
        "application_code","process_definition_key"
      }
    )
  })
public class ProcessInstanceSequenceEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;


    @NotBlank(message = "name is mandatory")
    @Column(name="name", nullable = false)
    private String name;


    @NotBlank(message = "prefix is mandatory")
    @Column(name="prefix", nullable = false)
    private String prefix;


    @NotNull(message = "checkDigitSize is mandatory")
    @Column(name="check_digit_size", nullable = false)
    private short checkDigitSize;


    @NotNull(message = "padding is mandatory")
    @Column(name="padding", nullable = false)
    private short padding;


    @Column(name="date_format")
    private String dateFormat;


    @NotNull(message = "nextNumber is mandatory")
    @Column(name="next_number", nullable = false)
    private Long nextNumber;


    @NotNull(message = "numberIncrement is mandatory")
    @Column(name="number_increment", nullable = false)
    private short numberIncrement;


    @NotBlank(message = "processDefinitionKey is mandatory")
    @Column(name="process_definition_key", nullable = false)
    private String processDefinitionKey;


    @NotBlank(message = "applicationCode is mandatory")
    @Column(name="application_code", nullable = false)
    private String applicationCode;


}
