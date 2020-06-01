package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor()
@Getter
public class ProfileDto {

    private String profileName;
    private List<SearchFieldDto> searchFields;


    public static ProfileDto from(Profile profile) {
        List<SearchFieldDto> searchFields = profile.getSearchFields().stream().map(SearchFieldDto::from).collect(Collectors.toList());
        return new ProfileDto(profile.getProfileName(), searchFields);

    }
}
