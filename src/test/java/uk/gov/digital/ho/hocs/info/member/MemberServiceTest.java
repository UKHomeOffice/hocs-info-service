package uk.gov.digital.ho.hocs.info.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.repositories.MemberRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RequestData requestData;

    private MemberService memberService;

    @Before
    public void setUp() {
        this.memberService = new MemberService(memberRepository, requestData);
    }

    @Test
    public void shouldReturnAllMembersInDB(){

        when(memberRepository.findAll()).thenReturn(memberList());

        Set<Member> repoResponse = memberService.getActiveMembersByCaseType();

        verify(memberRepository, times(1)).findAll();

        assertThat(repoResponse.size()).isEqualTo(6);
        assetMemberContainsCorrectElements(repoResponse,1, "member1");
        assetMemberContainsCorrectElements(repoResponse,2, "member2");
        assetMemberContainsCorrectElements(repoResponse,3, "member3");
        assetMemberContainsCorrectElements(repoResponse,4, "member4");
        assetMemberContainsCorrectElements(repoResponse,5, "member5");
        assetMemberContainsCorrectElements(repoResponse,6, "member6");

    }

    private void assetMemberContainsCorrectElements(Set<Member> members, int memberId, String DisplayName) {
        Member result1 = members.stream().filter(x -> Objects.equals(memberId, x.getId())).findAny().orElse(null);
        assertThat(result1).isNotNull();
        assertThat(result1.getDisplayName()).isEqualTo(DisplayName);
    }


    private Set<Member> memberList() {
        return new HashSet<Member>(){{
            add(new Member(1,"member1"));
            add(new Member(2,"member2"));
            add(new Member(3,"member3"));
            add(new Member(4,"member4"));
            add(new Member(5,"member5"));
            add(new Member(6,"member6"));
        }};
    }
}