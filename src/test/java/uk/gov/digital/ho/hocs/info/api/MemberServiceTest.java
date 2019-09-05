package uk.gov.digital.ho.hocs.info.api;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;
import uk.gov.digital.ho.hocs.info.domain.model.Constituency;
import uk.gov.digital.ho.hocs.info.domain.model.HouseAddress;
import uk.gov.digital.ho.hocs.info.domain.model.Member;
import uk.gov.digital.ho.hocs.info.client.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.domain.repository.ConstituencyRepository;
import uk.gov.digital.ho.hocs.info.domain.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private ConstituencyRepository constituencyRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ListConsumerService listConsumerService;

    private MemberService memberService;

    @Before
    public void setUp() {
        this.memberService = new MemberService(constituencyRepository, memberRepository, listConsumerService);
    }

    @Test
    public void shouldReturnAllActiveMembers() {

        memberService.getAllActiveMembers();

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
        when(memberRepository.findByUuid(any())).thenReturn(new Member(1L, "commons", "member1 title", "ext1", UUID.randomUUID(), LocalDateTime.now(), false, UUID.randomUUID(), new HouseAddress(), UUID.randomUUID(), "constituency", new Constituency()));

        memberService.getMember(any());
        verify(memberRepository, times(1)).findByUuid(any());
        verifyNoMoreInteractions(memberRepository);

    }

    @Test
    public void shouldThrowExceptionWhenMemberNotFound()  {
        UUID uuid = UUID.randomUUID();
        when(memberRepository.findByUuid(uuid)).thenReturn(null);
        try {
            memberService.getMember(uuid);
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