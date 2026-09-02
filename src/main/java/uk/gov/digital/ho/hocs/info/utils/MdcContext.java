package uk.gov.digital.ho.hocs.info.utils;

import org.slf4j.MDC;

import java.util.Map;

public final class MdcContext {

    private MdcContext() {
    }

    public static Scope use(Map<String, String> contextMap) {
        return new Scope(contextMap);
    }

    public static final class Scope implements AutoCloseable {

        private final Map<String, String> previousContextMap;

        private Scope(Map<String, String> contextMap) {
            this.previousContextMap = MDC.getCopyOfContextMap();
            if (contextMap != null) {
                MDC.setContextMap(contextMap);
            } else {
                MDC.clear();
            }
        }

        @Override
        public void close() {
            if (previousContextMap != null) {
                MDC.setContextMap(previousContextMap);
            } else {
                MDC.clear();
            }
        }
    }
}

