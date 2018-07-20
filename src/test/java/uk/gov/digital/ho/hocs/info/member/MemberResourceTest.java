package uk.gov.digital.ho.hocs.info.member;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;

import java.util.ArrayList;
import java.util.List;

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
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH_FROM_RETURN_VALUE")
    public void shouldReturnAllMembers() {

        when(memberService.getMembers()).thenReturn(memberList());

        ResponseEntity<GetMembersResponse> response = memberResource.getAllMembers(ROLE_SINGLE);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMembers().size()).isEqualTo(5);
    }



    private List<Member> memberList() {
        return new ArrayList<Member>(){{
            add(new Member(1,"member1"));
            add(new Member(2,"member2"));
            add(new Member(3,"member3"));
            add(new Member(4,"member4"));
            add(new Member(5,"member5"));
        }};
    }
}