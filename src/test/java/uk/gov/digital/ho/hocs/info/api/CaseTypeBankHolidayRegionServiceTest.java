package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeBankHolidayRegion;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeBankHolidayRegionRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeBankHolidayRegionServiceTest {

    CaseTypeBankHolidayRegionService caseTypeBankHolidayRegionService;

    @Mock
    public CaseTypeBankHolidayRegionRepository caseTypeBankHolidayRegionRepository;

    @Before
    public void setUp() {
        this.caseTypeBankHolidayRegionService =
                new CaseTypeBankHolidayRegionService(caseTypeBankHolidayRegionRepository);
    }

    @Test
    public void shouldReturnAListOfAllBankHolidayRegionsForACase() {
        // given
        final UUID caseTypeUuid = UUID.randomUUID();

        when(caseTypeBankHolidayRegionRepository.findAllByCaseTypeUuid(caseTypeUuid)).thenReturn(List.of(
                new CaseTypeBankHolidayRegion(caseTypeUuid, "NORTHERN_IRELAND"),
                new CaseTypeBankHolidayRegion(caseTypeUuid, "SCOTLAND")
        ));

        // when
        final List<String> result =
                caseTypeBankHolidayRegionService.getBankHolidayRegionsByCaseType(caseTypeUuid);

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.stream().filter(value -> value.equals("NORTHERN_IRELAND")).count()).isEqualTo(1);
        assertThat(result.stream().filter(value -> value.equals("SCOTLAND")).count()).isEqualTo(1);
    }
}
