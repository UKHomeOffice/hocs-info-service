package uk.gov.digital.ho.hocs.info.api.dto;

import org.junit.Test;
import uk.gov.digital.ho.hocs.info.domain.model.Deadline;
import uk.gov.digital.ho.hocs.info.domain.model.ExemptionDate;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class DeadlineDtoTest {

    @Test
    public void shouldBuildDeadlineDTOFromDeadlineObject() {
        Set<ExemptionDate> exemptionDates = new HashSet<>();
        exemptionDates.add(new ExemptionDate(1L, LocalDate.of(2018, 1, 1)));
        Deadline deadline = new Deadline(LocalDate.of(2018, 1, 1), 5, "Draft", exemptionDates);

        DeadlineDto deadlineDto = DeadlineDto.from(deadline);

        assertThat(deadlineDto.getDate()).isEqualTo(LocalDate.of(2018, 1, 8));
        assertThat(deadlineDto.getType()).isEqualTo("Draft");

    }
}