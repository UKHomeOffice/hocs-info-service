package uk.gov.digital.ho.hocs.info.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.RequestData;
import uk.gov.digital.ho.hocs.info.entities.Member;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.repositories.MemberRepository;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    @Autowired
    public MemberService(MemberRepository memberRepository, RequestData requestData) {
        this.memberRepository = memberRepository;
    }

    public Set<Member> getMembers(){
        log.debug("Requesting all Members");
        return memberRepository.findAll();
    }
}
