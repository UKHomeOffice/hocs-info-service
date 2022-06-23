package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.CaseDetailsField;


public class CaseDetailsFieldDtoTest {

    @Test
    public void from() {
        String caseType = "Type1";
        String name = "nameA";
        String component = "componentA";
        String props = "propertyA";

        CaseDetailsField caseDetailsField = new CaseDetailsField(1L, caseType, name, component, props, 1L);


        CaseDetailsFieldDto dto = CaseDetailsFieldDto.from(caseDetailsField);

        Assert.assertEquals("Name do not match", name, dto.getName());
        Assert.assertEquals("Component do not match", component, dto.getComponent());
        Assert.assertEquals("Props not match", props, dto.getProps());


    }
}
