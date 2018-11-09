package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.Template;

import java.util.UUID;

@Repository
public interface TemplateRepository extends CrudRepository<Template, String> {

    @Query(value = "SELECT t.* FROM template t WHERE t.case_type = ?1 AND t.deleted = FALSE", nativeQuery = true)
    Template findActiveTemplateByCaseType(String caseType);

    Template findTemplateByUuid(UUID templateUUID);

}
