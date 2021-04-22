package uk.gov.digital.ho.hocs.info.client.notifyclient.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@java.lang.SuppressWarnings("squid:S1068")
@Getter
@NoArgsConstructor
public class TeamRenameCommand {

    private String command = "team_rename";

    private UUID teamUUID;
    private String oldDisplayName;

    public TeamRenameCommand(UUID teamUUID, String oldDisplayName){
        this.teamUUID = teamUUID;
        this.oldDisplayName = oldDisplayName;
    }

}
