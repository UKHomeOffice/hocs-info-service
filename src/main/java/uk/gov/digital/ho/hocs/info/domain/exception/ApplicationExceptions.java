package uk.gov.digital.ho.hocs.info.domain.exception;

import uk.gov.digital.ho.hocs.info.application.LogEvent;
import uk.gov.digital.ho.hocs.info.api.dto.TeamDeleteActiveParentTopicsDto;

public interface ApplicationExceptions {
    class EntityCreationException extends RuntimeException {
        private final LogEvent event;

        public EntityCreationException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public EntityCreationException(String msg, Object... args) {
            super(String.format(msg, args));
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class EntityPermissionException extends RuntimeException {
        private final LogEvent event;

        public EntityPermissionException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public EntityPermissionException(String msg, Object... args) {
            super(String.format(msg, args));
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class ConstituencyCreationException extends RuntimeException {
        private final LogEvent event;

        public ConstituencyCreationException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public ConstituencyCreationException(String msg) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class CorrespondentCreationException extends RuntimeException {
        private final LogEvent event;

        public CorrespondentCreationException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public CorrespondentCreationException(String msg) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class EntityNotFoundException extends RuntimeException {
        private final LogEvent event;

        public EntityNotFoundException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public EntityNotFoundException(String msg, Object... args) {
            super(String.format(msg, args));
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class ResourceException extends RuntimeException {
        private final LogEvent event;

        ResourceException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class ResourceNotFoundException extends ResourceException {

        public ResourceNotFoundException(String msg, LogEvent event, Object... args) {
            super(msg, event, args);
        }

    }

    class ResourceServerException extends ResourceException {

        public ResourceServerException(String msg, LogEvent event, Object... args) {
            super(msg, event, args);
        }

    }

    class EntityAlreadyExistsException extends RuntimeException {
        private final LogEvent event;

        public EntityAlreadyExistsException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public EntityAlreadyExistsException(String msg) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }

    }

    class IngestException extends RuntimeException {
        private final LogEvent event;

        public IngestException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public IngestException(String msg) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }

    }

    class TeamDeleteException extends RuntimeException {
        private final LogEvent event;

        private TeamDeleteActiveParentTopicsDto teamDeleteActiveParentTopicsDto;

        public TeamDeleteException(TeamDeleteActiveParentTopicsDto teamDeleteActiveParentTopicsDto,String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
            this.teamDeleteActiveParentTopicsDto = teamDeleteActiveParentTopicsDto;
        }

        public TeamDeleteException(String msg,TeamDeleteActiveParentTopicsDto teamDeleteActiveParentTopicsDto) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
            this.teamDeleteActiveParentTopicsDto = teamDeleteActiveParentTopicsDto;

        }

        public TeamDeleteActiveParentTopicsDto getTeamDeleteActiveParentTopicsDto() {
            return teamDeleteActiveParentTopicsDto;
        }

        public LogEvent getEvent() {
            return event;
        }

    }

    class UnitDeleteException extends RuntimeException{
        private final LogEvent event;

        public UnitDeleteException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public UnitDeleteException(String msg, Object... args) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class UserRemoveException extends RuntimeException {
        private final LogEvent event;

        public UserRemoveException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public UserRemoveException(String msg, Object... args) {
            super(String.format(msg, args));
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class NominatedContactDeleteException extends RuntimeException{
        private final LogEvent event;

        public NominatedContactDeleteException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public NominatedContactDeleteException(String msg, Object... args) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }

    class TopicCreationException extends RuntimeException {
        private final LogEvent event;

        public TopicCreationException(String msg, LogEvent event, Object... args) {
            super(String.format(msg, args));
            this.event = event;
        }

        public TopicCreationException(String msg) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public LogEvent getEvent() {
            return event;
        }
    }


    class TopicUpdateException extends RuntimeException {

        private final LogEvent event;

        public TopicUpdateException(String msg, Object... args) {
            super(String.format(msg, args));
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }

        public TopicUpdateException(String msg) {
            super(msg);
            this.event = LogEvent.UNCAUGHT_EXCEPTION;
        }
        public LogEvent getEvent() {
            return event;
        }
    }

}