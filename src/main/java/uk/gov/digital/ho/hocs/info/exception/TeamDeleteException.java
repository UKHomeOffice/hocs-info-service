package uk.gov.digital.ho.hocs.info.exception;

import uk.gov.digital.ho.hocs.info.dto.TeamDeleteActiveParentTopicsDto;

public class TeamDeleteException extends RuntimeException {

    private TeamDeleteActiveParentTopicsDto teamDeleteActiveParentTopicsDto;

    public TeamDeleteException(String msg) {
        super(msg);
    }

    public TeamDeleteException(String msg, Object... args) {
        super(String.format(msg, args));
    }

    public TeamDeleteActiveParentTopicsDto getTeamDeleteActiveParentTopicsDto() {
        return teamDeleteActiveParentTopicsDto;
    }

    public TeamDeleteException(String msg, TeamDeleteActiveParentTopicsDto teamDeleteActiveParentTopicsDto, Object... args) {
        super(String.format(msg, args));
        this.teamDeleteActiveParentTopicsDto = teamDeleteActiveParentTopicsDto;

    }
}
