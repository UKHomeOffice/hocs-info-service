package uk.gov.digital.ho.hocs.info.unit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.dto.UnitDto;
import uk.gov.digital.ho.hocs.info.entities.Unit;
import uk.gov.digital.ho.hocs.info.exception.EntityAlreadyExistsException;
import uk.gov.digital.ho.hocs.info.repositories.UnitRepository;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import static net.logstash.logback.argument.StructuredArguments.value;
import static uk.gov.digital.ho.hocs.info.logging.LogEvent.EVENT;
import static uk.gov.digital.ho.hocs.info.logging.LogEvent.UNIT_CREATED;

@Service
@Slf4j
public class UnitService {

    private final UnitRepository unitRepository;

    @Autowired
    public UnitService(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    public void createUnit(UnitDto unit) {
        Unit newUnit = new Unit(unit.getDisplayName(),unit.getShortCode(), UUID.fromString(unit.getUuid()), true);
        try {
            unitRepository.save(newUnit);
        }
        catch(DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException("Unit already exists");
        }
        log.info("Unit with UUID {} created", unit.getUuid(), value(EVENT, UNIT_CREATED));
    }

    public Set<UnitDto> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unit -> UnitDto.fromWithoutTeams(unit)).collect(Collectors.toSet());
    }

    public Set<UnitDto> getAllUnitsForCaseType(String caseType) {
        return unitRepository.findAllActiveUnitsByCaseType(caseType).stream()
                .map(unit -> UnitDto.fromWithoutTeams(unit)).collect(Collectors.toSet());
    }
}

