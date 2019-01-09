package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Form;

import java.util.Set;

@Repository
public interface FormRepository extends CrudRepository<Form, String> {

    @Query(value = "SELECT fo.*, fi.* FROM form fo JOIN field fi ON fo.uuid = fi.form_uuid JOIN form_case_type fct ON fo.uuid = fct.form_uuid AND fct.case_type = ?1 AND fo.active = TRUE", nativeQuery = true)
    Set<Form> findAllActiveFormsByCaseType(String caseType);
    Set<Form> findAll();
    Form findByType(String type);
}