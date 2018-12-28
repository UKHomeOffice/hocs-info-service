package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.StageTypeEntity;

import java.util.Set;


@Repository
public interface StageTypeRepository extends CrudRepository<StageTypeEntity, String> {

    StageTypeEntity findByType(String type);
    Set<StageTypeEntity> findAllBy();

}