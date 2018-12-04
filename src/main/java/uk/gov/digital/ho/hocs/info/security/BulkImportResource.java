package uk.gov.digital.ho.hocs.info.security;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BulkImportResource {

    private BulkImportService bulkImportService;

    public BulkImportResource(BulkImportService bulkImportService) {
        this.bulkImportService = bulkImportService;
    }

    @PostMapping(value = "/admin/bulk-import")
    public ResponseEntity addUserToGroup() {
        bulkImportService.refreshAllGroups();
        return ResponseEntity.ok().build();
    }
}
