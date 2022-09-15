package uk.gov.digital.ho.hocs.info.api.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Field;
import uk.gov.digital.ho.hocs.info.security.AccessLevel;

public class FieldDtoTest {

    @Test
    public void from() {
        String fieldComponent = "text";
        String fieldName = "testField";
        String fieldLabel = "test field";
        String fieldValidation = "[]";
        String fieldProps = "{}";
        boolean fieldSummary = false;

        Field field = new Field(fieldComponent, fieldName, fieldLabel, fieldValidation, fieldProps, fieldSummary,
            AccessLevel.READ, null);

        FieldDto dto = FieldDto.from(field);

        Assert.assertEquals("Field component do not match", fieldComponent, dto.getComponent());
        Assert.assertEquals("Field name do not match", fieldName, dto.getName());
        Assert.assertEquals("Field label do not match", fieldLabel, dto.getLabel());
        Assert.assertEquals("Field validation do not match", fieldValidation, dto.getValidation());
        Assert.assertEquals("Field props do not match", fieldProps, dto.getProps());
        Assert.assertEquals("Field summary do not match", fieldSummary, dto.isSummary());
        Assert.assertNull("Field child do not match", dto.getChild());
    }

    @Test
    public void fromWithDecoratedProps() {
        String fieldComponent = "text";
        String fieldName = "testField";
        String fieldLabel = "test field";
        String fieldValidation = "[]";
        String fieldProps = "{\"choices\":[{\"label\":\"TestLabel\",\"value\":\"TestValue\"}],\"label\":\"test field\",\"name\":\"testField\"}";
        boolean fieldSummary = false;

        Field field = new Field(fieldComponent, fieldName, fieldLabel, fieldValidation, fieldProps, fieldSummary,
            AccessLevel.READ, null);
        ObjectMapper mapper = new ObjectMapper();
        FieldDto dto = FieldDto.fromWithDecoratedProps(field, mapper);

        Assert.assertEquals("Field component do not match", fieldComponent, dto.getComponent());
        Assert.assertEquals("Field name do not match", fieldName, dto.getName());
        Assert.assertEquals("Field label do not match", fieldLabel, dto.getLabel());
        Assert.assertEquals("Field validation do not match", fieldValidation, dto.getValidation());
        Assert.assertEquals("Field props do not match", fieldProps, dto.getProps());
        Assert.assertEquals("Field summary do not match", fieldSummary, dto.isSummary());
        Assert.assertNull("Field child do not match", dto.getChild());
    }

}
