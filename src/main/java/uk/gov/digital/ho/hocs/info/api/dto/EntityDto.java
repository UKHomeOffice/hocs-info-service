package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.model.Entity;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EntityDto {

    private String simpleName;
    private String uuid;
    @JsonRawValue
    private String data;

    public static EntityDto from(Entity entity) {
        return new EntityDto(entity.getSimpleName(), entity.getUuid() != null ? entity.getUuid().toString() : null, entity.getData());
    }

}
