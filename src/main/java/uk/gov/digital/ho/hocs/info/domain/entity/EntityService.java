package uk.gov.digital.ho.hocs.info.domain.entity;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class EntityService {

    private final EntityRepository entityRepository;

    @Autowired
    public EntityService(EntityRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    public Set<Entity> getBySimpleName(String owner, String ownerType, String list) {
        if (owner != null && ownerType != null && list != null) {
            return entityRepository.findBySimpleName(owner, ownerType, list);
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("owner, ownerType or list was null!");
        }
    }

    public List<Entity> getByEntityListName(String listName) {
        if (listName != null) {
            return entityRepository.findByEntityListSimpleName(listName);
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("listName was null!");
        }
    }
}