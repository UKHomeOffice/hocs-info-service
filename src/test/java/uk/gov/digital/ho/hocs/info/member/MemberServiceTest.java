package uk.gov.digital.ho.hocs.info.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.entities.HouseAddress;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;
import uk.gov.digital.ho.hocs.info.exception.IngestException;
import uk.gov.digital.ho.hocs.info.house.ingest.ListConsumerService;
import uk.gov.digital.ho.hocs.info.repositories.MemberRepository;

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
    public void shouldReturnAllActiveMembers() throws EntityPermissionException {

        when(caseTypeService.hasPermissionForCaseType(any())).thenReturn(true);

        memberService.getAllActiveMembers("MIN");

        verify(memberRepository, times(1)).findAllActiveMembers();
        verifyNoMoreInteractions(memberRepository);
    }


    @Test
    public void shouldUpdateOneMemberFromEachOfTheSixParliamentApis() throws IngestException {

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
    public void shouldReturnMemberWithAddress() throws EntityNotFoundException {
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
        } catch (EntityNotFoundException e) {
           assert(e.getLocalizedMessage().equals("Could not find member " + uuid));
        }
    }

    private Set<Member> getMembers() {
        Set<Member> members = new HashSet<>();
        members.add(new Member("House", "fullTitle", UUID.randomUUID(), "extRef"));
        return members;
    }

}