package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.WorkstackColumn;

import java.util.UUID;

public class WorkstackColumnDtoTest {

    @Test
    public void from() {
        String displayName = "ColumnABC";
        String dataAdapter = "adapter123";
        String renderer = "renderer";
        String dataValueKey = "valueKey";
        Boolean filterable = true;
        String headerClassName = "some css header name";
        String sortStrategy = "TestSortStrategy";
        WorkstackColumn workstackColumn = new WorkstackColumn(UUID.randomUUID(), displayName, dataAdapter, renderer,
            dataValueKey, filterable, headerClassName, sortStrategy);

        WorkstackColumnDto dto = WorkstackColumnDto.from(workstackColumn);

        Assert.assertEquals("Workstack column display name do not match", displayName, dto.getDisplayName());
        Assert.assertEquals("Workstack column data adapter do not match", dataAdapter, dto.getDataAdapter());
        Assert.assertEquals("Workstack column renderer do not match", renderer, dto.getRenderer());
        Assert.assertEquals("Workstack column data value key do not match", dataValueKey, dto.getDataValueKey());
        Assert.assertEquals("Workstack column isFilterable do not match", filterable, dto.isFilterable());
        Assert.assertEquals("Workstack column header class name do not match", headerClassName,
            dto.getHeaderClassName());
        Assert.assertEquals("Workstack column sort strategy do not match", sortStrategy, dto.getSortStrategy());

    }

}
