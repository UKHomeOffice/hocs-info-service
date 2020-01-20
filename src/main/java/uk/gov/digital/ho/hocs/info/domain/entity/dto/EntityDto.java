package uk.gov.digital.ho.hocs.info.domain.entity.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.domain.entity.Entity;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EntityDto {

    private String simpleName;
    @JsonRawValue
    private String data;

    public static EntityDto from(Entity entity) {
        return new EntityDto(entity.getSimpleName(), entity.getData());
    }

}
