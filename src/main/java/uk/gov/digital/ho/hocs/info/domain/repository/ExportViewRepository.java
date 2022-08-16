package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.ExportView;

import java.util.List;

@Repository
@Deprecated(forRemoval = true)
public interface ExportViewRepository extends CrudRepository<ExportView, Long> {

    List<ExportView> findAll();

    ExportView findByCode(String code);

}
