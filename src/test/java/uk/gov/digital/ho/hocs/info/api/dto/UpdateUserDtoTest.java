package uk.gov.digital.ho.hocs.info.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateUserDtoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void checkFirstNameMandatory() {
        UpdateUserDto updateUserDto = new UpdateUserDto(null, "lastname", true);
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(updateUserDto);
        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<UpdateUserDto> cv = constraintViolations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("First Name is mandatory");
    }

    @Test
    public void checkFirstNameNotEmptyString() {
        UpdateUserDto updateUserDto = new UpdateUserDto("", "lastname", true);
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(updateUserDto);
        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<UpdateUserDto> cv = constraintViolations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("First Name is mandatory");
    }

    @Test
    public void checkLastNameMandatory() {
        UpdateUserDto updateUserDto = new UpdateUserDto("firstname", null, true);
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(updateUserDto);
        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<UpdateUserDto> cv = constraintViolations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("Last Name is mandatory");
    }

    @Test
    public void checkLastNameNotEmptyString() {
        UpdateUserDto updateUserDto = new UpdateUserDto("firstname", "", true);
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(updateUserDto);
        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<UpdateUserDto> cv = constraintViolations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("Last Name is mandatory");
    }

    @Test
    public void checkEnabledMandatory() {
        UpdateUserDto updateUserDto = new UpdateUserDto("firstname", "lastname", null);
        Set<ConstraintViolation<UpdateUserDto>> constraintViolations = validator.validate(updateUserDto);
        assertThat(constraintViolations).hasSize(1);
        ConstraintViolation<UpdateUserDto> cv = constraintViolations.iterator().next();
        assertThat(cv.getMessage()).isEqualTo("Enabled is mandatory");
    }

}
