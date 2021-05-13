package uk.gov.digital.ho.hocs.info.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintValidatorContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@RunWith(SpringRunner.class)
public class EmailDomainValidatorTest {
    @Mock
    private final EmailDomainWhitelisted emailDomainWhitelisted = mock(EmailDomainWhitelisted.class);

    @Mock
    private final ConstraintValidatorContext constraintValidatorContext = mock(ConstraintValidatorContext.class);

    private final EmailDomainValidator emailDomainValidator = new EmailDomainValidator();

    @Before
    public void setUp() {
        emailDomainValidator.initialize(emailDomainWhitelisted);

        ReflectionTestUtils.setField(emailDomainValidator, "whitelistedDomains", List.of("example.com", "example.org"));
    }
    
    @Test
    public void whenEmailAddressIsValidButSecondInList_ShouldReturnTrue(){
        assertThat(emailDomainValidator.isValid("test@example.org", constraintValidatorContext))
                .isTrue();
    }

    @Test
    public void whenEmailAddressIsValidButNotInList_ShouldReturnFalse(){
        assertThat(emailDomainValidator.isValid("test@test.com", constraintValidatorContext))
                .isFalse();
    }

    @Test
    public void whenSubdomainEmailAddressIsValidButNotInList_ShouldReturnFalse(){
        assertThat(emailDomainValidator.isValid("test@sub.example.com", constraintValidatorContext))
                .isFalse();
    }

    @Test
    public void whenEmailAddressIsNull_ShouldReturnFalse(){
        assertThat(emailDomainValidator.isValid(null, constraintValidatorContext))
                .isFalse();
    }

    @Test
    public void whenEmailAddressIsEmpty_ShouldReturnFalse(){
        assertThat(emailDomainValidator.isValid("", constraintValidatorContext))
                .isFalse();
    }
}
