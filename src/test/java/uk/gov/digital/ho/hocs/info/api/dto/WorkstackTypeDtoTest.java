package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class WorkstackTypeDtoTest {

    @Test
    public void from() {

        List<WorkstackColumn> workstackColumns = Arrays.asList(new WorkstackColumn(10L, "columnName1", "adapter", "renderer", "valueKey", true, "cssClass"));
        WorkstackType workstackType = new WorkstackType(10L, "system", "some_type", workstackColumns);


        WorkstackTypeDto sut = WorkstackTypeDto.from(workstackType);

        Assert.assertEquals("Type value returns expected value", "some_type", sut.getType());
        Assert.assertEquals("workstackColumn contains expected ArrayList object", ArrayList.class, sut.getWorkstackColumns().getClass());

    }
}
