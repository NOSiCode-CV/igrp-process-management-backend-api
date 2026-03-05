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

@Audited
@Getter
@Setter
@IgrpEntity
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "t_iam_user_profile",
  indexes = {
    @Index(name = "idx_iam_user_profile_username", columnList = "username"),
    @Index(name = "idx_iam_user_profile_full_name", columnList = "full_name"),
    @Index(name = "idx_iam_user_profile_sub", columnList = "sub")
  }
)
public class IAMUserProfileEntity extends AuditEntity {

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private UUID id;


    @NotBlank(message = "username is mandatory")
    @Column(name="username", unique = true, nullable = false)
    private String username;


    @Column(name="email", unique = true)
    private String email;


    @Column(name="first_name")
    private String firstName;


    @Column(name="last_name")
    private String lastName;


    @Column(name="full_name")
    private String fullName;


    @NotBlank(message = "sub is mandatory")
    @Column(name="sub", unique = true, nullable = false)
    private String sub;


}
