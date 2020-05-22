package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;

import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WorkstackTypeDto {


    @JsonProperty("workstackType")
    private String type;

    @JsonProperty("workstackColumns")
    private List<WorkstackColumnDto> workstackColumns;

    public static WorkstackTypeDto from(WorkstackType type) {
        List<WorkstackColumnDto> workstackColumnDto = type.getWorkstackColumns().stream().map(WorkstackColumnDto::from).collect(Collectors.toList());
        return new WorkstackTypeDto(type.getType(), workstackColumnDto);
    }

}

