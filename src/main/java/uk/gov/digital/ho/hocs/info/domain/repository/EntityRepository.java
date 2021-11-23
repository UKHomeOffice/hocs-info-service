package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.Entity;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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

    @Query(value = "select e.* from entity e\n" +
            "                  join entity_list el on el.uuid = e.entity_list_uuid\n" +
            " where el.simple_name = ?1" +
            " and e.active = TRUE" +
            " order by e.sort_order", nativeQuery = true)
    List<Entity> findByEntityListSimpleName(String listSimpleName);

    @Query(value = "select Cast(el.uuid as varchar) id from entity_list el where el.simple_name = ?1", nativeQuery = true)
    String findEntityListUUIDBySimpleName(String listSimpleName);

    Optional<Entity> findBySimpleName(String simpleName);

    Entity findByUuid(UUID uuid);

    Optional<Entity> findBySimpleNameAndEntityListUUID(String simpleName, UUID entityListUuid);

    @Query(value = "select e.* from entity e where e.data = ?1 and e.entity_list_uuid = ?2", nativeQuery = true)
    List<Entity> findByDataAndEntityListUUID(String data, UUID entityListUuid);
}
