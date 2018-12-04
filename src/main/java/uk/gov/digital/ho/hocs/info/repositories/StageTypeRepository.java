package uk.gov.digital.ho.hocs.info.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.entities.StageTypeEntity;

import java.util.Set;


@Repository
public interface StageTypeRepository extends CrudRepository<StageTypeEntity, String> {


    Set<StageTypeEntity> findAllBy();

}