package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.model.ExtractFieldSortOrder;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.domain.repository.ExtractFieldSortOrderRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class ExtractService {

    private final ExtractFieldSortOrderRepository extractFieldSortOrderRepository;
    private final SchemaService schemaService;

    @Autowired
    public ExtractService(
            ExtractFieldSortOrderRepository extractFieldSortOrderRepository,
            SchemaService schemaService) {
        this.extractFieldSortOrderRepository = extractFieldSortOrderRepository;
        this.schemaService = schemaService;
    }


    public List<String> getAllReportingFieldsForCaseType(String caseType) {
        Stream<Field> fields = Stream.concat(schemaService.getExtractOnlyFields().stream(), schemaService.getAllReportingFieldsForCaseType(caseType).sorted(Comparator.comparingLong(Field::getId)));
        List<String> unsortedFields = fields.map(Field::getName).collect(Collectors.toCollection(ArrayList::new));

        Stream<ExtractFieldSortOrder> sortOrderFields = extractFieldSortOrderRepository.findAllByCaseTypeOrderBySortOrder(caseType).stream();
        List<String> sortOrder = sortOrderFields.map(ExtractFieldSortOrder::getFieldName).collect(Collectors.toCollection(ArrayList::new));

        List<String> results = new ArrayList<>();

        for (String field : sortOrder) {
            if (unsortedFields.contains(field)) {
                results.add(field);
            }
        }

        for (String field : unsortedFields) {
            if (!results.contains(field)) {
                results.add(field);
            }
        }
        return results;
    }

}
