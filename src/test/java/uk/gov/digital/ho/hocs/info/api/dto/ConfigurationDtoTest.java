package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Configuration;
import uk.gov.digital.ho.hocs.info.domain.model.SearchField;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;


public class ConfigurationDtoTest {

    @Test
    public void from() {
        String systemName = "system";
        String systemDisplayName = "Demo System";
        String docLabelString = "label1,label2";

        String columnDisplayName = "ColumnABC";
        String columnDataAdapter = "adapter123";
        String columnRenderer = "renderer";
        String columnDataValueKey = "valueKey";
        Boolean columnFilterable = true;
        String columnHeaderClassName = "some css header name";

        String searchFieldDisplayName = "Field";
        String searchFieldComponent = "checkbox";
        String searchFieldValidation = "validationRuleC";
        String searchFieldProps = "props";

        List<WorkstackColumn> workstackColumns = Arrays.asList(new WorkstackColumn(10L, systemName, columnDisplayName, columnDataAdapter, columnRenderer, columnDataValueKey, columnFilterable, columnHeaderClassName));

        List<SearchField> searchFields = Arrays.asList(new SearchField(10L, systemName, searchFieldDisplayName, searchFieldComponent, searchFieldValidation, searchFieldProps));
        String readOnlyCaseViewAdapter = "Adapter";
        Configuration configuration = new Configuration(systemName, systemDisplayName, docLabelString, true, true, workstackColumns, searchFields, true, readOnlyCaseViewAdapter);


        ConfigurationDto dto = ConfigurationDto.from(configuration);

        Assert.assertEquals("Display name do not match", systemDisplayName, dto.getDisplayName());
        Assert.assertEquals("Document labels do not match", new ArrayList<>(Arrays.asList(docLabelString.split(","))), dto.getDocumentLabels());
        Assert.assertEquals("Bulk Create setting is incorrect", true, dto.isBulkCreateEnabled());
        Assert.assertEquals("Deadline Enabled setting is incorrect", true, dto.isDeadlinesEnabled());
        Assert.assertEquals("AutoCreateAndAllocateEnabled setting is incorrect", true, dto.isAutoCreateAndAllocateEnabled());
        Assert.assertEquals("ReadOnlyCaseViewAdapter setting is incorrect", readOnlyCaseViewAdapter, dto.getReadOnlyCaseViewAdapter());

        Assert.assertEquals("There should be 1 workstack column", 1, dto.getWorkstackColumns().size());
        Assert.assertEquals("Workstack column display name do not match", columnDisplayName, dto.getWorkstackColumns().get(0).getDisplayName());
        Assert.assertEquals("Workstack column data adapter do not match", columnDataAdapter, dto.getWorkstackColumns().get(0).getDataAdapter());
        Assert.assertEquals("Workstack column renderer do not match", columnRenderer, dto.getWorkstackColumns().get(0).getRenderer());
        Assert.assertEquals("Workstack column data value key do not match", columnDataValueKey, dto.getWorkstackColumns().get(0).getDataValueKey());
        Assert.assertEquals("Workstack column isFilterable do not match", columnFilterable, dto.getWorkstackColumns().get(0).isFilterable());
        Assert.assertEquals("Workstack column header class name do not match", columnHeaderClassName, dto.getWorkstackColumns().get(0).getHeaderClassName());

        Assert.assertEquals("There should be 1 search field", 1, dto.getSearchFields().size());
        Assert.assertEquals("Search field name do not match", searchFieldDisplayName, dto.getSearchFields().get(0).getName());
        Assert.assertEquals("Search field component do not match", searchFieldComponent, dto.getSearchFields().get(0).getComponent());
        Assert.assertEquals("Search field validation do not match", searchFieldValidation, dto.getSearchFields().get(0).getValidation());
        Assert.assertEquals("Search field props do not match", searchFieldProps, dto.getSearchFields().get(0).getProps());



    }
}
