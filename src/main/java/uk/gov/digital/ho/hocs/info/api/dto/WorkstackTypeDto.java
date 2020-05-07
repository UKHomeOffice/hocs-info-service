package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;

import java.util.List;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WorkstackTypeDto {


    @JsonProperty("workstackType")
    private String type;

    @JsonProperty("workstackColumns")
    private List<WorkstackColumn> workstackColumns;

    public static WorkstackTypeDto from(WorkstackType type) {
        return new WorkstackTypeDto(type.getType(), type.getWorkstackColumns());
    }

}
