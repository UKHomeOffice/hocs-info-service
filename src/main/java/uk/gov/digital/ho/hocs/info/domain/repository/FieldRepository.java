package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import uk.gov.digital.ho.hocs.info.domain.model.Field;

import java.util.List;

public interface FieldRepository extends CrudRepository<Field, String> {

    @Deprecated(forRemoval = true)
    @Query(value = "SELECT * from field f LEFT JOIN field_screen fs ON f.uuid = fs.field_uuid LEFT JOIN screen_schema ss ON ss.uuid = fs.schema_uuid WHERE ss.type = ?1 ORDER BY sort_order ASC",
           nativeQuery = true)
    List<Field> findAllBySchemaType(String schemaType);

}
