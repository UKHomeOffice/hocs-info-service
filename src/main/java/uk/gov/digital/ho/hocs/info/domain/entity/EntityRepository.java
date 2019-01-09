package uk.gov.digital.ho.hocs.info.domain.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EntityRepository extends CrudRepository<Entity, Long> {


    @Query(value = "select e3.* from entity_list el\n" +
            "                  join entity e on e.entity_list_uuid = el.uuid\n" +
            "                  join entity_relation er on e.uuid = er.parent_entity_uuid\n" +
            "                  join entity e2 on e2.uuid = er.entity_uuid\n" +
            "                  join entity_list el2 on e2.entity_list_uuid = el2.uuid\n" +
            "                  join entity_relation er2 on e2.uuid = er2.parent_entity_uuid\n" +
            "                  join entity e3 on e3.uuid = er2.entity_uuid\n" +
            "where el.simple_name = ?1 and e.simple_name = ?2 and el2.simple_name = ?3", nativeQuery = true)
    Set<Entity> findBySimpleName(String owner, String ownerType, String list);
}