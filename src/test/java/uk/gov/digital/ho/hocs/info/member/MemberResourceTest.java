package uk.gov.digital.ho.hocs.info.member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.gov.digital.ho.hocs.info.casetype.CaseTypeService;
import uk.gov.digital.ho.hocs.info.dto.GetMembersResponse;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.exception.EntityNotFoundException;
import uk.gov.digital.ho.hocs.info.exception.EntityPermissionException;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MemberResourceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private CaseTypeService caseTypeService;

    private MemberResource memberResource;

    private final String[] ROLE_SINGLE = {"DCU"};
    private final String[] ROLE_MULTI = {"DCU","UKVI"};

    @Before
    public void setUp() {
        memberResource = new MemberResource(memberService, caseTypeService);
    }

    @Test
    public void shouldReturnAllMembers() throws EntityPermissionException {

        when(memberService.getActiveMembersByCaseType(any())).thenReturn(memberList());

        ResponseEntity<GetMembersResponse> response = memberResource.getAllMembers(any());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getMembers().size()).isEqualTo(5);
    }

    private Set<Member> memberList() {
        return new HashSet<Member>(){{
            add(new Member(1,"member1","member1",UUID.randomUUID().toString()));
            add(new Member(2,"member2","member2",UUID.randomUUID().toString()));
            add(new Member(3,"member3","member3",UUID.randomUUID().toString()));
            add(new Member(4,"member4","member4",UUID.randomUUID().toString()));
            add(new Member(5,"member5","member5",UUID.randomUUID().toString()));
        }};
    }
}