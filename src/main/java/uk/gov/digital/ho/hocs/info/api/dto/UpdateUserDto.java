package uk.gov.digital.ho.hocs.info.api.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@SuppressWarnings("squid:S1068")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateUserDto {

    @NotBlank(message = "First Name is mandatory")
    private String firstName;

    @NotBlank(message = "Last Name is mandatory")
    private String lastName;

    @NotNull(message = "Enabled is mandatory")
    private Boolean enabled;

}



