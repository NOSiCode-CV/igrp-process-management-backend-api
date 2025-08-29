/* THIS FILE WAS GENERATED AUTOMATICALLY BY iGRP STUDIO. */
/* DO NOT MODIFY IT BECAUSE IT COULD BE REWRITTEN AT ANY TIME. */

package cv.igrp.platform.process.management.processdefinition.application.dto;

import cv.igrp.framework.stereotype.IgrpDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor


@IgrpDTO
public class ProcessSequenceDTO  {



  private UUID id ;


  private String name ;


  private String prefix ;


  private short checkDigitSize ;


  private short padding ;


  private String dateFormat ;


  private Long nextNumber ;


  private short numberIncrement ;


  private UUID processDefinitionId ;

}
