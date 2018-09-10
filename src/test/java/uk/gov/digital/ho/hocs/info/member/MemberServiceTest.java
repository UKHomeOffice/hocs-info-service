package uk.gov.digital.ho.hocs.info.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.repositories.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RequestData requestData;

    @Mock
    private CaseTypeService caseTypeService;

    private MemberService memberService;

    @Before
    public void setUp() {
        this.memberService = new MemberService(memberRepository, caseTypeService, requestData);
    }

    @Test
    public void shouldReturnAllActiveMembersForSpecificCaseType() throws EntityPermissionException {

        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        memberService.getAllActiveMembersByCaseType("MIN");

        verify(memberRepository, times(1)).findAllActiveMembersForCaseType(any());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    public void shouldReturnAllActiveMembers() throws EntityPermissionException {

        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        memberService.getAllActiveMembers("MIN");

        verify(memberRepository, times(1)).findAllActiveMembers();
        verifyNoMoreInteractions(memberRepository);
    }

}