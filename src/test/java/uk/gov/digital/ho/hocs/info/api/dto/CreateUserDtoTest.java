package uk.gov.digital.ho.hocs.info.api.dto;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorFactoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.gov.digital.ho.hocs.info.utils.EmailDomainValidator;

public class CreateUserDtoTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void checkEmailMandatory() {
        CreateUserDto createUserDto = new CreateUserDto(null, "firstname", "lastname");
        Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
        assertThat(constraintViolations.stream().anyMatch(x -> x.getMessage().equals("Email is mandatory"))).isTrue();
    }

    @Test
    public void checkEmailNotEmptyString() {
        CreateUserDto createUserDto = new CreateUserDto("", "firstname", "lastname");
        Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
        assertThat(constraintViolations.stream().anyMatch(x -> x.getMessage().equals("Email is mandatory"))).isTrue();
    }

    @Test
    public void checkFirstNameMandatory() {
        CreateUserDto createUserDto = new CreateUserDto("test@example.co.uk", null, "lastname");
        Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
        assertThat(
            constraintViolations.stream().anyMatch(x -> x.getMessage().equals("First Name is mandatory"))).isTrue();
    }

    @Test
    public void checkFirstNameNotEmptyString() {
        CreateUserDto createUserDto = new CreateUserDto("test@example.co.uk", "", "lastname");
        Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
        ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();

        assertThat(
            constraintViolations.stream().anyMatch(x -> x.getMessage().equals("First Name is mandatory"))).isTrue();
    }

    @Test
    public void checkLastNameMandatory() {
        CreateUserDto createUserDto = new CreateUserDto("test@example.co.uk", "firstname", null);
        Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
        assertThat(
            constraintViolations.stream().anyMatch(x -> x.getMessage().equals("Last Name is mandatory"))).isTrue();
    }

    @Test
    public void checkLastNameNotEmptyString() {
        CreateUserDto createUserDto = new CreateUserDto("test@example.co.uk", "firstname", "");
        Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
        assertThat(
            constraintViolations.stream().anyMatch(x -> x.getMessage().equals("Last Name is mandatory"))).isTrue();
    }

}
