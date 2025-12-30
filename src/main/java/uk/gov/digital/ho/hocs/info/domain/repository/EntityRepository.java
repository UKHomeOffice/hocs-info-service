package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Modifying;
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

    @Query(
        value = """
            SELECT e3.* FROM entity_list el
            JOIN entity e ON e.entity_list_uuid = el.uuid
            JOIN entity_relation er ON e.uuid = er.parent_entity_uuid
            JOIN entity e2 ON e2.uuid = er.entity_uuid
            JOIN entity_list el2 ON e2.entity_list_uuid = el2.uuid
            JOIN entity_relation er2 ON e2.uuid = er2.parent_entity_uuid
            JOIN entity e3 ON e3.uuid = er2.entity_uuid
            WHERE el.simple_name = ?1 AND e.simple_name = ?2 AND el2.simple_name = ?3""",
        nativeQuery = true
    )
    Set<Entity> findBySimpleName(String owner, String ownerType, String list);

    @Query(
        value = """
            SELECT e.* FROM entity e
            JOIN entity_list el ON el.uuid = e.entity_list_uuid
            WHERE el.simple_name = ?1
            ORDER BY e.sort_order""",
        nativeQuery = true
    )
    List<Entity> findByEntityListSimpleName(String listSimpleName);

    @Query(
        value = """
            SELECT CAST(el.uuid AS VARCHAR) id
            FROM entity_list el
            WHERE el.simple_name = ?1""",
        nativeQuery = true
    )
    String findEntityListUUIDBySimpleName(String listSimpleName);

    Optional<Entity> findBySimpleName(String simpleName);

    Entity findByUuid(UUID uuid);

    Optional<Entity> findBySimpleNameAndEntityListUUID(String simpleName, UUID entityListUuid);

    @Query(
        value = """
            SELECT e.*
            FROM entity e
            WHERE e.data = ?1
              AND e.entity_list_uuid = ?2""",
        nativeQuery = true
    )
    List<Entity> findByDataAndEntityListUUID(String data, UUID entityListUuid);

    @Modifying
    @Query(
        value = """
            UPDATE entity e
SET sort_order = t.new_sort_order
FROM (
       SELECT
         uuid,
         10 * ROW_NUMBER() OVER (PARTITION BY entity_list_uuid ORDER BY data->>'title') AS new_sort_order
       FROM entity
     ) t
WHERE e.uuid = t.uuid;
            """,
        nativeQuery = true
    )
    void resortByTitle();
}
