package uk.gov.digital.ho.hocs.info.domain.model;

import org.junit.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class StageTypeEntityTest {

    @Test
    public void shouldAddTeamsToEntity() {
        UUID uuid = UUID.randomUUID();
        UUID caseTypeUuid = UUID.randomUUID();
        Team team1 = new Team("Team 2", true);

        StageTypeEntity stageTypeEntity = new StageTypeEntity(
                uuid,
                "displayName",
                "shortCode",
                "type",
                caseTypeUuid,
                1,
                2,
                1,
                true,
                team1,
                false
        );

        assertThat(stageTypeEntity.getUuid()).isEqualTo(uuid);
        assertThat(stageTypeEntity.getDisplayName()).isEqualTo("displayName");
        assertThat(stageTypeEntity.getShortCode()).isEqualTo("shortCode");
        assertThat(stageTypeEntity.getType()).isEqualTo("type");
        assertThat(stageTypeEntity.getCaseTypeUUID()).isEqualTo(caseTypeUuid);
        assertThat(stageTypeEntity.getDeadline()).isEqualTo(1);
        assertThat(stageTypeEntity.getDeadlineWarning()).isEqualTo(2);
        assertThat(stageTypeEntity.isActive()).isTrue();
        assertThat(stageTypeEntity.getTeam()).isEqualTo(team1);
    }
}
