package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.DocumentTag;

import java.util.List;

@Repository
@Deprecated(forRemoval = true)
public interface DocumentTagRepository extends CrudRepository<DocumentTag, String> {

    @Query(value = "SELECT dt.* FROM document_tag dt INNER JOIN case_type ct ON ct.uuid = dt.case_type_uuid WHERE ct.type = ?1 ORDER BY dt.sort_order",
           nativeQuery = true)
    List<DocumentTag> findByCaseType(String caseType);

}
