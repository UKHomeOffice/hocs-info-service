package uk.gov.digital.ho.hocs.info.client.notifyclient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@SuppressWarnings("squid:S1068")
@Getter
public class TeamActiveCommand extends TeamCommand {

    private UUID teamUUID;
    private Boolean currentActiveStatus;

    public TeamActiveCommand() {
        super("team_active");
    }

    public TeamActiveCommand(UUID teamUUID, Boolean currentActiveStatus) {
        this();
        this.teamUUID = teamUUID;
        this.currentActiveStatus = currentActiveStatus;
    }

}
