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
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MemberResourceTest {

    @Mock
    private MemberService memberService;

    private MemberResource memberResource;

    @Before
    public void setUp() {
        memberResource = new MemberResource(memberService);
    }

    @Test
    public void shouldReturnAllMembers() throws EntityPermissionException {

        when(memberService.getAllActiveMembers(any())).thenReturn(memberList());

        ResponseEntity<GetMembersResponse> response = memberResource.getAllActiveMembers(any());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMembers().size()).isEqualTo(2);
        verify(memberService, times(1)).getAllActiveMembers(any());
        verifyNoMoreInteractions(memberService);
    }

    private Set<Member> memberList() {
        return new HashSet<Member>(){{
            add(new Member(1l,"commons","member1 title","ext1" ,UUID.randomUUID(),LocalDateTime.now(),false));
            add(new Member(2l,"commons","member2 title","ext2" ,UUID.randomUUID(),LocalDateTime.now(),false));
        }};
    }
}