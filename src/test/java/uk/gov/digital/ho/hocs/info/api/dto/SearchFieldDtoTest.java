package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.SearchField;

public class SearchFieldDtoTest {

    @Test
    public void from() {
        String profileName = "DCU";
        String searchFieldDisplayName = "Field";
        String searchFieldComponent = "checkbox";
        String searchFieldValidation = "validationRuleC";
        String searchFieldProps = "props";

        SearchField searchField = new SearchField(10L, profileName, searchFieldDisplayName, searchFieldComponent,
            searchFieldValidation, searchFieldProps);

        SearchFieldDto dto = SearchFieldDto.from(searchField);

        Assert.assertEquals("Search field name do not match", searchFieldDisplayName, dto.getName());
        Assert.assertEquals("Search field component do not match", searchFieldComponent, dto.getComponent());
        Assert.assertEquals("Search field validation do not match", searchFieldValidation, dto.getValidation());
        Assert.assertEquals("Search field props do not match", searchFieldProps, dto.getProps());

    }

}
