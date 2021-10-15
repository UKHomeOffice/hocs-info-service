package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class WorkstackTypeDtoTest {

    @Test
    public void from() {

        String displayName = "columnName1";
        String dataAdapter = "adapter";
        String renderer = "some_renderer";
        String dataValueKey = "some_valueKey";
        Boolean isFilterable = true;
        String cssClass = "some_css_class";
        String sortStrategy = "TestSortStrategy";

        List<WorkstackColumn> workstackColumns = Arrays.asList(new WorkstackColumn(UUID.randomUUID(), displayName, dataAdapter, renderer, dataValueKey, isFilterable, cssClass, sortStrategy));
        WorkstackType workstackType = new WorkstackType(10L, "system", "some_type", workstackColumns);


        WorkstackTypeDto sut = WorkstackTypeDto.from(workstackType);

        Assert.assertEquals("Type value returns expected value", "some_type", sut.getType());
        Assert.assertEquals("workstackColumn displayName contains expected string", displayName, sut.getWorkstackColumns().get(0).getDisplayName());
        Assert.assertEquals("workstackColumn dataAdapter contains expected string", dataAdapter, sut.getWorkstackColumns().get(0).getDataAdapter());
        Assert.assertEquals("workstackColumn renderer contains expected string", renderer, sut.getWorkstackColumns().get(0).getRenderer());
        Assert.assertEquals("workstackColumn dataValueKey contains expected string", dataValueKey, sut.getWorkstackColumns().get(0).getDataValueKey());
        Assert.assertEquals("workstackColumn isFilterable contains expected boolean", isFilterable, sut.getWorkstackColumns().get(0).isFilterable());
        Assert.assertEquals("workstackColumn headerClassName contains expected string", cssClass, sut.getWorkstackColumns().get(0).getHeaderClassName());
        Assert.assertEquals("workstackColumn sortStrategy contains expected string", sortStrategy, sut.getWorkstackColumns().get(0).getSortStrategy());

    }
}
