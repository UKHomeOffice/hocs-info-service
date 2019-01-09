package uk.gov.digital.ho.hocs.info.security;
import uk.gov.digital.ho.hocs.info.application.LogEvent;

public interface SecurityExceptions {
    class PermissionCheckException extends RuntimeException {
        LogEvent event;
        public PermissionCheckException(String s, LogEvent event) {
            super(s);
            this.event = event;
        }
        public LogEvent getEvent() {return event;}
    }


}
