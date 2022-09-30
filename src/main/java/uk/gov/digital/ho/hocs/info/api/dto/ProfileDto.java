package uk.gov.digital.ho.hocs.info.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import uk.gov.digital.ho.hocs.info.domain.model.Profile;

@AllArgsConstructor()
@Getter
public class ProfileDto {

    private String profileName;

    private boolean summaryDeadlineEnabled;

    public static ProfileDto from(Profile profile) {
        return new ProfileDto(profile.getProfileName(), profile.isSummaryDeadlinesEnabled());
    }

}
