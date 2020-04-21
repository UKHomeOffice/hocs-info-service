package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.ExtractFieldSortOrder;

import java.util.List;
import java.util.stream.Stream;

@Repository
public interface ExtractFieldSortOrderRepository extends CrudRepository<ExtractFieldSortOrder, Long> {

    List<ExtractFieldSortOrder> findAllByCaseTypeOrderBySortOrder(String caseType);

}
