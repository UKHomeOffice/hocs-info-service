package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Template;

import java.util.List;
import java.util.UUID;

@Repository
public interface TemplateRepository extends CrudRepository<Template, String> {


    @Query(value = "SELECT t.* FROM template t WHERE t.case_type = ?1 AND t.deleted = FALSE", nativeQuery = true)
    List<Template> findActiveTemplatesByCaseType(String caseType);

    @Query(value = "SELECT t.* FROM template t WHERE t.uuid = ?1 AND t.deleted = FALSE", nativeQuery = true)
    Template findActiveTemplateByUuid(UUID uuid);

    @Query(value = "SELECT t.* FROM template t WHERE t.deleted = FALSE", nativeQuery = true)
    List<Template> findActiveTemplates();
}
