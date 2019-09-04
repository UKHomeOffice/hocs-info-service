package uk.gov.digital.ho.hocs.info.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uk.gov.digital.ho.hocs.info.domain.model.TeamLink;

import java.util.Set;
import java.util.UUID;

@Repository
public interface TeamLinkRepository extends CrudRepository<TeamLink, String> {

    Set<TeamLink> findAllByLinkUUIDAndLinkType(UUID linkUUID, String linkType);

    TeamLink findByLinkUUIDAndLinkTypeAndCaseTypeAndStageType(UUID linkUUID, String linkType, String caseType, String stageType);

}
