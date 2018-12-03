package uk.gov.digital.ho.hocs.info.logging;

public enum LogEvent {

    UNIT_CREATED,
    TEAM_CREATED,
    TEAM_ADDED_TO_UNIT,
    TEAM_RENAMED,
    TEAM_PERMISSIONS_UPDATED,
    USER_ADDED_TO_TEAM,
    KEYCLOAK_FAILURE,
    BULK_IMPORT_STARTED,
    BULK_IMPORT_SUCCESS,
    BULK_IMPORT_FAILURE;

    public static final String EVENT = "event_id";
}
