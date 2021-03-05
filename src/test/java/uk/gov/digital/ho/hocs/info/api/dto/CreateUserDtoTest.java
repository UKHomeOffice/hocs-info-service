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
      assertThat(constraintViolations).hasSize(1);
      ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
      assertThat(cv.getMessage()).isEqualTo("Email is mandatory");
    }

  @Test
  public void checkEmailNotEmptyString() {
    CreateUserDto createUserDto = new CreateUserDto("", "firstname", "lastname");
    Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
    assertThat(constraintViolations).hasSize(1);
    ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
    assertThat(cv.getMessage()).isEqualTo("Email is mandatory");
  }

  @Test
  public void checkEmailWithNonWhitelistedDomain() {
    CreateUserDto createUserDto = new CreateUserDto("test@bbc.co.uk", "firstname", "lastname");
    Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
    assertThat(constraintViolations).hasSize(1);
    ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
    assertThat(cv.getMessage()).isEqualTo("Email domain not supported");
  }

  @Test
  public void checkFirstNameMandatory() {
    CreateUserDto createUserDto = new CreateUserDto("test@homeoffice.gov.uk", null, "lastname");
    Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
    assertThat(constraintViolations).hasSize(1);
    ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
    assertThat(cv.getMessage()).isEqualTo("First Name is mandatory");
  }

  @Test
  public void checkFirstNameNotEmptyString() {
    CreateUserDto createUserDto = new CreateUserDto("test@homeoffice.gov.uk", "", "lastname");
    Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
    assertThat(constraintViolations).hasSize(1);
    ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
    assertThat(cv.getMessage()).isEqualTo("First Name is mandatory");
  }

  @Test
  public void checkLastNameMandatory() {
    CreateUserDto createUserDto = new CreateUserDto("test@homeoffice.gov.uk", "firstname", null);
    Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
    assertThat(constraintViolations).hasSize(1);
    ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
    assertThat(cv.getMessage()).isEqualTo("Last Name is mandatory");
  }

  @Test
  public void checkLastNameNotEmptyString() {
    CreateUserDto createUserDto = new CreateUserDto("test@homeoffice.gov.uk", "firstname", "");
    Set<ConstraintViolation<CreateUserDto>> constraintViolations = validator.validate(createUserDto);
    assertThat(constraintViolations).hasSize(1);
    ConstraintViolation<CreateUserDto> cv = constraintViolations.iterator().next();
    assertThat(cv.getMessage()).isEqualTo("Last Name is mandatory");
  }

}
