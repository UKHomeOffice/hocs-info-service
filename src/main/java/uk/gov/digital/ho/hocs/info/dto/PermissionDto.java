package uk.gov.digital.ho.hocs.info.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.entities.Permission;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;



@Getter
public class PermissionDto {

    @JsonCreator
    public PermissionDto(@JsonProperty("caseTypeCode") String caseTypeCode, @JsonProperty("accessLevel") AccessLevel accessLevel) {
        this.caseTypeCode = caseTypeCode;
        this.accessLevel = accessLevel;
    }
    @JsonProperty("caseTypeCode")
    private String caseTypeCode;

    @JsonProperty("accessLevel")
    private AccessLevel accessLevel;

    public static PermissionDto from(Permission permission) {
        return new PermissionDto(permission.getCaseType().getType(),
                AccessLevel.from(permission.getAccessLevel().getLevel()));
    }
}
