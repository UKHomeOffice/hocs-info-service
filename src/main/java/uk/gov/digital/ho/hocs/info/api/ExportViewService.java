package uk.gov.digital.ho.hocs.info.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.ExportView;
import uk.gov.digital.ho.hocs.info.domain.repository.ExportViewRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExportViewService {

    private final ExportViewRepository exportViewRepository;

    @Autowired
    public ExportViewService(ExportViewRepository exportViewRepository) {
        this.exportViewRepository = exportViewRepository;
    }

    public List<ExportViewDto> getAllExportViews() {
        log.debug("Getting all ExportViews");
        List<ExportView> views = exportViewRepository.findAll();
        log.info("Got {} ExportViews", views.size());

        List<ExportView> results = new ArrayList<>();
        for (ExportView exportView : views) {
            results.add(exportView);

        }
        return results.stream().map(ExportView::toDto).collect(Collectors.toList());
    }

    public ExportViewDto getExportView(String code) {
        log.debug("Getting ExportView for {}", code);
        ExportView exportView = exportViewRepository.findByCode(code);
        if (exportView != null) {
            log.info("Got {} ExportView", exportView.getDisplayName());
            return exportView.toDto();
        }
        log.warn("ExportView for code {} not found", code);
        throw new ApplicationExceptions.EntityNotFoundException("ExportView not found for %s", code);
    }


}
