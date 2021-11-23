package uk.gov.digital.ho.hocs.info.api.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import uk.gov.digital.ho.hocs.info.utils.EmailDomainWhitelisted;

@java.lang.SuppressWarnings("squid:S1068")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CreateUserDto {

    @NotBlank(message = "Email is mandatory")
    @EmailDomainWhitelisted(message = "Email domain not supported")
    private String email;

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

}



