package cv.igrp.platform.process.management.area.domain.service;

import cv.igrp.platform.process.management.area.domain.models.Area;
import cv.igrp.platform.process.management.area.domain.models.AreaFilter;
import cv.igrp.platform.process.management.area.domain.models.AreaProcess;
import cv.igrp.platform.process.management.area.domain.models.AreaProcessFilter;
import cv.igrp.platform.process.management.area.domain.repository.AreaProcessRepository;
import cv.igrp.platform.process.management.area.domain.repository.AreaRepository;
import cv.igrp.platform.process.management.shared.domain.exceptions.IgrpResponseStatusException;
import cv.igrp.platform.process.management.shared.domain.models.PageableLista;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AreaService {

  private final AreaRepository areaRepository;
  private final AreaProcessRepository areaProcessRepository;

  public AreaService(AreaRepository areaRepository,
                     AreaProcessRepository areaProcessRepository) {
    this.areaRepository = areaRepository;
    this.areaProcessRepository = areaProcessRepository;
  }

  public Area getAreaById(UUID id) {
    return areaRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Area not found. ID: ", id));
  }

  public PageableLista<Area> getAllAreas(AreaFilter areaFilter) {
    return areaRepository.findAll(areaFilter);
  }

  public Area createArea(Area area) {
    return areaRepository.save(area);
  }

  public Area updateArea(UUID id, Area newArea) {
    Area oldArea = getAreaById(id);
    oldArea.update(newArea);
    return areaRepository.save(oldArea);
  }

  public void deleteArea(UUID id) {
    Area area = getAreaById(id);
    area.delete();
    areaRepository.delete(id);
  }

  public AreaProcess createProcessDefinition(UUID areaId, AreaProcess areaProcess) {
    Area area = getAreaById(areaId);
    Optional<AreaProcess> process = area.getProcess(areaProcess.getReleaseId());
    if(process.isEmpty()) {
      area.add(areaProcess);
    }else{
      areaProcess = process.get();
      areaProcess.reActivate();
    }
    return areaProcessRepository.save(areaProcess);
  }

  public void removeProcessDefinition(UUID areaId, UUID areaProcessId) {
    Area area = getAreaById(areaId);
    AreaProcess areaProcess = getAreaProcessById(areaProcessId);
    area.remove(areaProcess);
    areaProcessRepository.delete(areaProcess.getId().getValue());
  }

  public AreaProcess getAreaProcessById(UUID id) {
    return areaProcessRepository.findById(id)
        .orElseThrow(() -> IgrpResponseStatusException.notFound("Area process definition not found. ID: ", id));
  }

  public PageableLista<AreaProcess> getAllAreaProcess(AreaProcessFilter areaFilter) {
    return areaProcessRepository.findAll(areaFilter);
  }

}
