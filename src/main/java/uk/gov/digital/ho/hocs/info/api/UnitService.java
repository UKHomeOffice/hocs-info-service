package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.domain.repository.TeamRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.*;

@Service
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;

    private final TeamRepository teamRepository;

    @Autowired
    public UnitService(UnitRepository unitRepository, TeamRepository teamRepository) {
        this.unitRepository = unitRepository;
        this.teamRepository = teamRepository;
    }

    public void createUnit(UnitDto unit) {
        Unit newUnit = new Unit(unit.getDisplayName(),unit.getShortCode(), true);
        try {
            unitRepository.save(newUnit);
        }
        catch(DataIntegrityViolationException e) {
            throw new ApplicationExceptions.EntityAlreadyExistsException("Unit already exists");
        }
        log.info("Unit with UUID {} created", unit.getUuid(), value(EVENT, UNIT_CREATED));
    }

    public Set<UnitDto> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(UnitDto::fromWithoutTeams).collect(Collectors.toSet());
    }

    public Set<UnitDto> getAllUnitsForCaseType(String caseType) {
        return unitRepository.findAllActiveUnitsByCaseType(caseType).stream()
                .map(UnitDto::fromWithoutTeams).collect(Collectors.toSet());
    }

    Unit getUnit(UUID unitUUID){
        log.debug("Getting Unit: {}", unitUUID);
        Unit unit = unitRepository.findByUuid(unitUUID);
        if (unit == null){
            log.info("Unit with UUID {} not found", unitUUID, value(EVENT, UNIT_NOT_FOUND));
            throw new ApplicationExceptions.EntityNotFoundException("Unit with UUID {} not found", unitUUID);
        } else {
            log.info("Retrieved unit with UUID {}", unitUUID, value(EVENT, UNIT_RETRIEVED));
            return unit;
        }
    }

    public void deleteUnit(UUID unitUUID) {
        log.debug("Deleting Unit: {}", unitUUID);
        Set<Team> teams = teamRepository.findActiveTeamsByUnitUuid(unitUUID);
        if (teams.isEmpty()){
            Unit unit = getUnit(unitUUID);
            unit.setActive(false);
            unitRepository.save(unit);
        } else {
            throw new ApplicationExceptions.UnitDeleteException("Unable to delete Unit {}, active teams are allocated to unit", unitUUID);
        }
        log.info("Deleted Unit: {}", unitUUID, value(EVENT, UNIT_DELETED));
    }
}

