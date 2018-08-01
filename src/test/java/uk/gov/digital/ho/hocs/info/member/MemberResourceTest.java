package uk.gov.digital.ho.hocs.info.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MemberResourceTest {

    @Mock
    private MemberService memberService;

    private MemberResource memberResource;

    private final String[] ROLE_SINGLE = {"DCU"};
    private final String[] ROLE_MULTI = {"DCU","UKVI"};

    @Before
    public void setUp() {
        memberResource = new MemberResource(memberService);
    }

    @Test
    public void shouldReturnAllMembers() {

        when(memberService.getMembers()).thenReturn(memberList());

        ResponseEntity<GetMembersResponse> response = memberResource.getAllMembers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMembers().size()).isEqualTo(5);
    }

    @Test
    public void shouldErrorOnNoMembers() {

        when(memberService.getMembers()).thenThrow(new EntityNotFoundException("No members!"));

        ResponseEntity<GetMembersResponse> response = memberResource.getAllMembers();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }



    private Set<Member> memberList() {
        return new HashSet<Member>(){{
            add(new Member(1,"member1"));
            add(new Member(2,"member2"));
            add(new Member(3,"member3"));
            add(new Member(4,"member4"));
            add(new Member(5,"member5"));
        }};
    }
}