package uk.gov.digital.ho.hocs.info.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.digital.ho.hocs.info.api.dto.ExportViewDto;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@Deprecated(forRemoval = true)
public class ExportViewResource {

    private final ExportViewService exportViewService;

    @Autowired
    public ExportViewResource(ExportViewService exportViewService) {
        this.exportViewService = exportViewService;
    }

    @GetMapping(value = "/export", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<ExportViewDto>> getAllExportViews() {
        return ResponseEntity.ok(exportViewService.getAllExportViews());
    }

    @GetMapping(value = "/export/{code}", produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<ExportViewDto> getExportView(@PathVariable String code) {
        return ResponseEntity.ok(exportViewService.getExportView(code));
    }

}
