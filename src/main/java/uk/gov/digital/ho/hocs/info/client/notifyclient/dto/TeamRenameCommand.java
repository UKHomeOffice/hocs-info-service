package uk.gov.digital.ho.hocs.info.client.notifyclient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@java.lang.SuppressWarnings("squid:S1068")
@Getter
public class TeamRenameCommand extends TeamCommand {

    private UUID teamUUID;

    private String oldDisplayName;

    public TeamRenameCommand() {
        super("team_rename");
    }

    public TeamRenameCommand(UUID teamUUID, String oldDisplayName) {
        this();
        this.teamUUID = teamUUID;
        this.oldDisplayName = oldDisplayName;
    }

}
