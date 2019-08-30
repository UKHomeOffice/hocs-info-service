package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.TopicTeam;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TopicTeamRepository extends CrudRepository<TopicTeam, String> {

    Set<TopicTeam> findAllByTopicUUID(UUID topicUUID);

    TopicTeam findByTopicUUIDAndCaseTypeAndStageType(UUID topicUUID, String caseType, String stageType);

}
