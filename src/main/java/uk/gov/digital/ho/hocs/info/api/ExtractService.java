package uk.gov.digital.ho.hocs.info.api;

import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ExtractService {

    private final SchemaService schemaService;

    public ExtractService(
            SchemaService schemaService) {
        this.schemaService = schemaService;
    }

    @Deprecated(forRemoval = true)
    public List<String> getAllReportingFieldsForCaseType(String caseType) {
        Stream<Field> sortedFields = Stream.concat(schemaService.getExtractOnlyFields().stream(),
                schemaService.getAllReportingFieldsForCaseType(caseType).sorted(Comparator.comparingLong(Field::getId)));

        return sortedFields.map(Field::getName)
                .distinct()
                .toList();
    }

}
