package cv.igrp.platform.process.management.area.mappers;

import cv.igrp.platform.process.management.area.application.dto.AreaDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaListaPageDTO;
import cv.igrp.platform.process.management.area.application.dto.AreaRequestDTO;
import cv.igrp.platform.process.management.area.application.dto.ProcessDefinitionDTO;
import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.shared.domain.models.Code;
import cv.igrp.platform.process.management.shared.domain.models.Identifier;
import cv.igrp.platform.process.management.shared.domain.models.Name;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import cv.igrp.platform.process.management.shared.infrastructure.persistence.entity.AreaEntity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class AreaMapper {

  private final AreaProcessMapper areaProcessMapper;

  public AreaMapper(AreaProcessMapper areaProcessMapper) {
    this.areaProcessMapper = areaProcessMapper;
  }

  public Area toModel(AreaRequestDTO areaRequestDTO) {
      return Area.builder()
          .code(Code.create(areaRequestDTO.getCode()))
          .name(Name.create(areaRequestDTO.getName()))
          .applicationBase(Code.create(areaRequestDTO.getApplicationBase()))
          .areaId(areaRequestDTO.getParentId() != null ? Identifier.create(areaRequestDTO.getParentId()) : null)
          .build();
    }

    public AreaDTO toDTO(Area area) {
      AreaDTO areaDTO = new AreaDTO();
      areaDTO.setId(area.getId().getValue());
      areaDTO.setName(area.getName().getValue());
      areaDTO.setApplicationBase(area.getApplicationBase().getValue());
      areaDTO.setCode(area.getCode().getValue());
      areaDTO.setStatus(area.getStatus());
      areaDTO.setStatusDesc(area.getStatus().getDescription());
      areaDTO.setAreaId(area.getAreaId() != null ? area.getAreaId().getValue() : null);
      areaDTO.setUpdatedAt(area.getUpdatedAt());
      areaDTO.setCreatedAt(area.getCreatedAt());
      areaDTO.setCreatedBy(area.getCreatedBy());
      areaDTO.setUpdatedBy(area.getUpdatedBy());
      List<ProcessDefinitionDTO> processDefinitions = area.getProcess()
          .stream()
          .map(areaProcessMapper::toDTO)
          .toList();
      areaDTO.setProcess(processDefinitions);
      return areaDTO;
    }

    public AreaEntity toEntity(Area area) {
      AreaEntity areaEntity = new AreaEntity();
      areaEntity.setId(area.getId().getValue());
      areaEntity.setName(area.getName().getValue());
      areaEntity.setApplicationBase(area.getApplicationBase().getValue());
      areaEntity.setCode(area.getCode().getValue());
      areaEntity.setStatus(area.getStatus());
      if(area.getAreaId() != null) {
        AreaEntity parent = new AreaEntity();
        parent.setId(area.getAreaId().getValue());
        areaEntity.setAreaId(parent);
      }
      return areaEntity;
    }

    public Area toModel(AreaEntity areaEntity) {
      Area area = Area.builder()
          .id(Identifier.create(areaEntity.getId()))
          .name(Name.create(areaEntity.getName()))
          .applicationBase(Code.create(areaEntity.getApplicationBase()))
          .code(Code.create(areaEntity.getCode()))
          .status(areaEntity.getStatus())
          .createdAt(areaEntity.getCreatedDate())
          .updatedAt(areaEntity.getLastModifiedDate())
          .createdBy(areaEntity.getCreatedBy())
          .updatedBy(areaEntity.getLastModifiedBy())
          .areaId(areaEntity.getAreaId() != null ? Identifier.create(areaEntity.getAreaId().getId()) : null)
          .build();
      if(areaEntity.getProcessdefinitions() != null) {
        areaEntity.getProcessdefinitions().forEach(areaProcessEntity -> {
          area.add(areaProcessMapper.toModel(areaProcessEntity));
        });
      }
      return area;
    }

  public AreaListaPageDTO toDTO(PageableLista<Area> areaPageableLista ) {
    AreaListaPageDTO areaListaPageDTO = new AreaListaPageDTO();
    areaListaPageDTO.setPageNumber(areaPageableLista.getPageNumber());
    areaListaPageDTO.setPageSize(areaPageableLista.getPageSize());
    areaListaPageDTO.setTotalElements(areaPageableLista.getTotalElements());
    areaListaPageDTO.setTotalPages(areaPageableLista.getTotalPages());
    areaListaPageDTO.setFirst(areaPageableLista.isFirst());
    areaListaPageDTO.setLast(areaPageableLista.isLast());
    List<AreaDTO> content = areaPageableLista.getContent()
        .stream()
        .map(this::toDTO)
        .toList();
    areaListaPageDTO.setContent(content);
    return areaListaPageDTO;
  }

}
