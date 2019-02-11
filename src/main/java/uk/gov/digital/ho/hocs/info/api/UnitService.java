package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Unit;
import uk.gov.digital.ho.hocs.info.domain.repository.UnitRepository;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.application.LogEvent.UNIT_CREATED;

@Service
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;

    @Autowired
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
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
}

