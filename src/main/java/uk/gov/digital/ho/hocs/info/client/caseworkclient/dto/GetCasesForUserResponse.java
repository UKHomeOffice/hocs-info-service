package uk.gov.digital.ho.hocs.info.client.caseworkclient.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetCasesForUserResponse {

    @JsonProperty("caseUUIDs")
    private Set<UUID> caseUUIDs;

}