package uk.gov.digital.ho.hocs.info.team;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.entities.Team;
import uk.gov.digital.ho.hocs.info.repositories.TeamRepository;

import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Team getTeamForMember(int memberId) {
        return teamRepository.getTeamFromMemberId(memberId);
    }

    public Optional<Team> getTeamFromId(int id){
        return teamRepository.findById(id);
    }

    public Team getTeamForTopic(int topicId) {
        return teamRepository.getTeamFromTopicId(topicId);
    }

}
