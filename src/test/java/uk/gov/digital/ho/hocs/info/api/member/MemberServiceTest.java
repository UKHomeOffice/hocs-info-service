package uk.gov.digital.ho.hocs.info.api.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.application.RequestData;
import uk.gov.digital.ho.hocs.info.api.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

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
    public void shouldReturnAllActiveMembers() {

        memberService.getAllActiveMembers("MIN");

        verify(memberRepository, times(1)).findAllActiveMembers();
        verifyNoMoreInteractions(memberRepository);
    }


    @Test
    public void shouldUpdateOneMemberFromEachOfTheSixParliamentApis() {

        when(listConsumerService.createCommonsFromUKParliamentAPI()).thenReturn(getMembers());
        when(listConsumerService.createFromScottishParliamentAPI()).thenReturn(getMembers());
        when(listConsumerService.createFromWelshAssemblyAPI()).thenReturn(getMembers());
        when(listConsumerService.createLordsFromUKParliamentAPI()).thenReturn(getMembers());
        //        when(listConsumerService.createFromIrishAssemblyAPI()).thenReturn(getMembers());
        //        when(listConsumerService.createFromEuropeanParliamentAPI()).thenReturn(getMembers());

        memberService.updateWebMemberLists();

        verify(memberRepository, times(4)).findByExternalReference(any());
        verify(memberRepository, times(4)).save(any());
        verifyNoMoreInteractions(memberRepository);
    }

    @Test
    public void shouldReturnMemberWithAddress() {
        when(memberRepository.findByUuid(any())).thenReturn(new Member(1l, "commons", "member1 title", "ext1", UUID.randomUUID(), LocalDateTime.now(), false, UUID.randomUUID(), new HouseAddress()));

        memberService.getMemberAndAddress(any());
        verify(memberRepository, times(1)).findByUuid(any());
        verifyNoMoreInteractions(memberRepository);

    }

    @Test
    public void shouldThrowExceptionWhenMemberNotFound()  {
        UUID uuid = UUID.randomUUID();
        when(memberRepository.findByUuid(uuid)).thenReturn(null);
        try {
            memberService.getMemberAndAddress(uuid);
        } catch (ApplicationExceptions.EntityNotFoundException e) {
           assert(e.getLocalizedMessage().equals("Could not find member " + uuid));
        }
    }

    private Set<Member> getMembers() {
        Set<Member> members = new HashSet<>();
        members.add(new Member("House", "fullTitle", UUID.randomUUID(), "extRef"));
        return members;
    }

}