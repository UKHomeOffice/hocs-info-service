package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.ExportView;
import uk.gov.digital.ho.hocs.info.domain.repository.ExportViewRepository;
import uk.gov.digital.ho.hocs.info.security.KeycloakService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportViewService {

    private RequestData requestData;
    private final KeycloakService keycloakService;
    private final ExportViewRepository exportViewRepository;

    @Autowired
    public ExportViewService(RequestData requestData, KeycloakService keycloakService, ExportViewRepository exportViewRepository) {
        this.requestData = requestData;
        this.keycloakService = keycloakService;
        this.exportViewRepository = exportViewRepository;
    }

    public List<ExportViewDto> getAllExportViews() {
        log.debug("Getting all ExportViews");
        List<ExportView> views = exportViewRepository.findAll();
        log.info("Got {} ExportViews", views.size());

        UUID userUUID = requestData.userIdUUID();
        Set<String> userRoles = keycloakService.getRolesForUser(userUUID);
        List<ExportView> results = new ArrayList<>();
        for (ExportView exportView : views) {
            if (StringUtils.hasText(exportView.getRequiredPermission()) && !userRoles.contains(exportView.getRequiredPermission())) {
                log.debug("Skipping ExportView due to permission not assigned to the user, user {}, permission {}", userUUID, exportView.getRequiredPermission());
            } else {
                results.add(exportView);
            }
        }
        return results.stream().map(ExportView::toDto).collect(Collectors.toList());
    }

    public ExportViewDto getExportView(String code) {
        log.debug("Getting ExportView for {}", code);
        ExportView exportView = exportViewRepository.findByCode(code);
        if (exportView != null) {
            log.info("Got {} ExportView", exportView.getDisplayName());
            if (StringUtils.hasText(exportView.getRequiredPermission())) {
                Set<String> userRoles = keycloakService.getRolesForUser(requestData.userIdUUID());
                if (!userRoles.contains(exportView.getRequiredPermission())) {
                    throw new ApplicationExceptions.EntityPermissionException("No permission to view %s", code);
                }
            }

            return exportView.toDto();
        }
        log.warn("ExportView for code {} not found", code);
        throw new ApplicationExceptions.EntityNotFoundException("ExportView not found for %s", code);
    }


}
