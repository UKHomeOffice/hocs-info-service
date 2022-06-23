package uk.gov.digital.ho.hocs.info.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.api.dto.CaseDetailsFieldDto;
import uk.gov.digital.ho.hocs.info.domain.model.CaseDetailsField;
import uk.gov.digital.ho.hocs.info.domain.repository.CaseDetailsFieldRepository;

import javax.persistence.PersistenceException;
import java.util.List;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class CaseDetailsFieldServiceTest {

    @Mock
    private CaseDetailsFieldRepository caseDetailsFieldRepository;


    private CaseDetailsFieldService caseDetailsFieldService;

    private final String caseType = "Type1";


    @Before
    public void setUp() {
        this.caseDetailsFieldService = new CaseDetailsFieldService(caseDetailsFieldRepository);
    }

    @Test
    public void getCaseDetailsFieldsByCaseType() {

        String nameA = "nameA";
        String componentA = "componentA";
        String propertyA = "propertyA";
        String nameB = "nameB";
        String componentB = "componentB";
        String propertyB = "propertyB";

        CaseDetailsField fieldA = new CaseDetailsField(1L, caseType, nameA, componentA, propertyA, 1L);
        CaseDetailsField fieldB = new CaseDetailsField(2L, caseType, nameB, componentB, propertyB, 2L);

        when(caseDetailsFieldRepository.findByCaseTypeOrderBySortOrder(caseType)).thenReturn(List.of(fieldA, fieldB));

        List<CaseDetailsFieldDto> results = caseDetailsFieldService.getCaseDetailsFieldsByCaseType(caseType);

        Assert.assertEquals("There should be 2 fields returned", 2, results.size());
        Assert.assertEquals("Field A name does not match", nameA, results.get(0).getName());
        Assert.assertEquals("Field B name does not match", nameB, results.get(1).getName());
        Assert.assertEquals("Field A component does not match", componentA, results.get(0).getComponent());
        Assert.assertEquals("Field B component does not match", componentB, results.get(1).getComponent());
        Assert.assertEquals("Field A props does not match", propertyA, results.get(0).getProps());
        Assert.assertEquals("Field B props does not match", propertyB, results.get(1).getProps());

        verify(caseDetailsFieldRepository).findByCaseTypeOrderBySortOrder(caseType);
        verifyNoMoreInteractions(caseDetailsFieldRepository);

    }

    @Test
    public void getCaseDetailsFieldsByCaseType_EmptyList() {

        when(caseDetailsFieldRepository.findByCaseTypeOrderBySortOrder(caseType)).thenReturn(List.of());

        List<CaseDetailsFieldDto> results = caseDetailsFieldService.getCaseDetailsFieldsByCaseType(caseType);

        Assert.assertEquals("There should be 0 fields returned", 0, results.size());

        verify(caseDetailsFieldRepository).findByCaseTypeOrderBySortOrder(caseType);
        verifyNoMoreInteractions(caseDetailsFieldRepository);
    }

    @Test(expected = PersistenceException.class)
    public void getCaseDetailsFieldsByCaseType_throwsException() {

        when(caseDetailsFieldRepository.findByCaseTypeOrderBySortOrder(caseType)).thenThrow(new PersistenceException("Test Exception"));

        caseDetailsFieldService.getCaseDetailsFieldsByCaseType(caseType);

    }

}
