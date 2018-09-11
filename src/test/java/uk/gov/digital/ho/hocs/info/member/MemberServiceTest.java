package uk.gov.digital.ho.hocs.info.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.exception.IngestException;
import uk.gov.digital.ho.hocs.info.house.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.repositories.MemberRepository;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RequestData requestData;

    @Mock
    private CaseTypeService caseTypeService;

    @Mock
    private ListConsumerService listConsumerService;

    private MemberService memberService;

    @Before
    public void setUp() {
        this.memberService = new MemberService(memberRepository, caseTypeService, listConsumerService, requestData);
    }

    @Test
    public void shouldReturnAllActiveMembers() throws EntityPermissionException {

        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        memberService.getAllActiveMembers("MIN");

        verify(memberRepository, times(1)).findAllActiveMembers();
        verifyNoMoreInteractions(memberRepository);
    }


    @Test
    public void shouldUpdateOneMemberFromEachOfTheSixParliamentApis() throws IngestException {

        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);
        when(listConsumerService.createCommonsFromUKParliamentAPI()).thenReturn(getMembers());
        when(listConsumerService.createFromEuropeanParliamentAPI()).thenReturn(getMembers());
        when(listConsumerService.createFromIrishAssemblyAPI()).thenReturn(getMembers());
        when(listConsumerService.createFromScottishParliamentAPI()).thenReturn(getMembers());
        when(listConsumerService.createFromWelshAssemblyAPI()).thenReturn(getMembers());
        when(listConsumerService.createLordsFromUKParliamentAPI()).thenReturn(getMembers());

        memberService.updateWebMemberLists();

        verify(memberRepository, times(6)).findByExternalReference(any());
        verify(memberRepository, times(6)).save(any());
        verifyNoMoreInteractions(memberRepository);
    }

    private Set<Member> getMembers() {
        Set<Member> members = new HashSet<>();
        members.add(new Member("House", "fullTitle", "extRef"));
        return members;
    }

}