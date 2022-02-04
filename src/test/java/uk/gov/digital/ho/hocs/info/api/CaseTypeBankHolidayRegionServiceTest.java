package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.model.CaseType;
import uk.gov.digital.ho.hocs.info.domain.model.CaseTypeBankHolidayRegion;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeBankHolidayRegionRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseTypeRepository;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseTypeBankHolidayRegionServiceTest {

    CaseTypeBankHolidayRegionService caseTypeBankHolidayRegionService;

    @Mock
    public CaseTypeBankHolidayRegionRepository caseTypeBankHolidayRegionRepository;

    @Mock
    public CaseTypeRepository caseTypeRepository;

    @Before
    public void setUp() {
        this.caseTypeBankHolidayRegionService =
                new CaseTypeBankHolidayRegionService(caseTypeBankHolidayRegionRepository, caseTypeRepository);
    }

    @Test
    public void shouldReturnAListOfAllBankHolidayRegionsForACase() {
        // given
        final CaseType caseType =
                new CaseType("CASE_TYPE", "TYPE", null, null, null, false, false, null);

        when(caseTypeRepository.findByType(caseType.getType())).thenReturn(caseType);
        when(caseTypeBankHolidayRegionRepository.findAllByCaseTypeUuid(caseType.getUuid())).thenReturn(List.of(
                new CaseTypeBankHolidayRegion(caseType.getUuid(), "NORTHERN_IRELAND"),
                new CaseTypeBankHolidayRegion(caseType.getUuid(), "SCOTLAND")
        ));

        // when
        final List<String> result =
                caseTypeBankHolidayRegionService.getBankHolidayRegionsByCaseType(caseType.getType());

        // then
        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.stream().filter(value -> value.equals("NORTHERN_IRELAND")).count()).isEqualTo(1);
        assertThat(result.stream().filter(value -> value.equals("SCOTLAND")).count()).isEqualTo(1);
    }
}
