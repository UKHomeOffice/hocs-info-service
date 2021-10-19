package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Team;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
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

    private final TeamService teamService;

    @Autowired
    public UnitService(UnitRepository unitRepository, TeamService teamService) {
        this.unitRepository = unitRepository;
        this.teamService = teamService;
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

    Unit getUnitForTeam(UUID teamUuid){
        log.debug("Getting Unit for team: {}", teamUuid);
        Team team = teamService.getTeam(teamUuid);

        if (team == null){
            log.info("Team with UUID {} not found", teamUuid, value(EVENT, TEAM_NOT_FOUND));
            throw new ApplicationExceptions.EntityNotFoundException("Team with UUID {} not found", teamUuid);
        } else {
            log.info("Retrieved unit for team with UUID {}", teamUuid, value(EVENT, UNIT_RETRIEVED));
            return team.getUnit();
        }
    }


    public void deleteUnit(UUID unitUUID) {
        log.debug("Deleting Unit: {}", unitUUID);
        Set<Team> teams = teamService.findActiveTeamsByUnitUuid(unitUUID);
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

