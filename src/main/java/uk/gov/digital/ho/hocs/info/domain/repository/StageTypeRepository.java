package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;

import java.util.Set;
import java.util.UUID;

@Repository
public interface StageTypeRepository extends CrudRepository<StageTypeEntity, String> {

    StageTypeEntity findByType(String type);

    Set<StageTypeEntity> findAllByCaseTypeUUID(UUID caseTypeUUID);

    Set<StageTypeEntity> findAllBy();

}