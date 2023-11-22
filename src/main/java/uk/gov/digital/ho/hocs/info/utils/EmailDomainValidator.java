package uk.gov.digital.ho.hocs.info.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class EmailDomainValidator implements ConstraintValidator<EmailDomainWhitelisted, String> {

    @Value("${user.email.whitelist}")
    private List<String> whitelistedDomains = Collections.emptyList();

    ;

    @Override
    public void initialize(EmailDomainWhitelisted emailDomainWhitelisted) {}

    @Override
    public boolean isValid(String emailAddress, ConstraintValidatorContext cxt) {
        return emailAddress != null && !emailAddress.isBlank() && whitelistedDomains.stream().anyMatch(
            domain -> emailAddress.toLowerCase().endsWith(String.format("@%s", domain)));
    }

}
