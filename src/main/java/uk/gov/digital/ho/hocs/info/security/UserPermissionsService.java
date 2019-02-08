package uk.gov.digital.ho.hocs.info.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import java.nio.BufferUnderflowException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserPermissionsService {

    private RequestData requestData;

    @Autowired
    public UserPermissionsService(RequestData requestData) {
        this.requestData = requestData;
    }

    public Set<UUID> getUserTeams() {
        return Arrays.stream(requestData.groupsArray())
                .map(group -> getUUIDFromBase64(group))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private UUID getUUIDFromBase64(String uuid) {
        if (uuid.startsWith("/")) {
            uuid = uuid.substring(1);
        }
        try {
            return Base64UUID.Base64StringToUUID(uuid);
        } catch (BufferUnderflowException e) {
            return null;
        }
    }
}
