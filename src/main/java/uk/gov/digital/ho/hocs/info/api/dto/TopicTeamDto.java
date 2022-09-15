package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.TopicTeam;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TopicTeamDto {

    @JsonProperty("label")
    private String displayName;

    @JsonProperty("value")
    private UUID uuid;

    @JsonProperty("teams")
    private Set<TeamDto> teams;

    public static TopicTeamDto from(TopicTeam topicTeam) {
        Set<TeamDto> teams = topicTeam.getTeams().stream().map(t -> TeamDto.fromWithoutPermissions(t)).collect(
            Collectors.toSet());
        return new TopicTeamDto(topicTeam.getDisplayName(), topicTeam.getUuid(), teams);
    }

}
