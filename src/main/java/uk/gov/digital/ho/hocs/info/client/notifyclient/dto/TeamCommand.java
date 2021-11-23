package uk.gov.digital.ho.hocs.info.client.notifyclient.dto;

import lombok.Getter;

@Getter
public abstract class TeamCommand {
    private final String command;

    // default to prevent external package misuse
    TeamCommand(String command) {
        this.command = command;
    }
}
