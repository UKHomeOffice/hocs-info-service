package uk.gov.digital.ho.hocs.info.api;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.digital.ho.hocs.info.api.dto.EntityDto;
import uk.gov.digital.ho.hocs.info.domain.model.Entity;
import uk.gov.digital.ho.hocs.info.domain.repository.EntityRepository;
import uk.gov.digital.ho.hocs.info.domain.exception.ApplicationExceptions;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Entity> getByEntityListNameAndActive(String listName, Boolean activeOnly) {
        if (listName != null) {
            if (activeOnly) {
                return entityRepository.findByEntityListSimpleName(listName);
            } else {
                return entityRepository.findAllByEntityListSimpleName(listName);
            }
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("listName was null!");
        }
    }

    public void createEntity(String listName, EntityDto entityDto) {

        String entityListUUID = entityRepository.findEntityListUUIDBySimpleName(listName);

        if (entityListUUID != null) {
            Optional<Entity> existingEntity = entityRepository.findBySimpleNameAndEntityListUUID(
                entityDto.getSimpleName(), UUID.fromString(entityListUUID));

            if (existingEntity.isPresent()) {
                throw new ApplicationExceptions.EntityAlreadyExistsException(
                    "entity with this simple name already exists!");
            }
        } else {
            throw new ApplicationExceptions.EntityNotFoundException("EntityList not found for: %s ", listName);
        }

        Entity newEntity = new Entity(null, UUID.randomUUID(), entityDto.getSimpleName(), entityDto.getData(),
            UUID.fromString(entityListUUID), true, 10);

        log.info("Creating entity {} with data: {}, simpleName: {}", newEntity.getUuid(), newEntity.getData(),
            newEntity.getSimpleName());
        entityRepository.save(newEntity);

    }

    public Entity getEntity(String uuid) {
        return entityRepository.findByUuid(UUID.fromString(uuid));
    }

    public Entity getEntityBySimpleName(String simpleName) throws Exception {
        return entityRepository.findBySimpleName(simpleName).orElseThrow(
            () -> new ApplicationExceptions.EntityNotFoundException(
                "Entity with simpleName " + simpleName + " not found."));
    }

    public void updateEntity(String listName, EntityDto entityDto) {
        String entityListUUID = entityRepository.findEntityListUUIDBySimpleName(listName);

        Entity entity = entityRepository.findByUuid(UUID.fromString(entityDto.getUuid()));

        if (StringUtils.isNotEmpty(entityListUUID) && entity.getEntityListUUID().equals(
            UUID.fromString(entityListUUID))) {
            List<Entity> existingEntities = entityRepository.findByDataAndEntityListUUID(entityDto.getData(),
                UUID.fromString(entityListUUID));

            existingEntities = existingEntities.stream().filter(
                e -> !Objects.equals(e.getUuid(), UUID.fromString(entityDto.getUuid()))).collect(Collectors.toList());

            if (!existingEntities.isEmpty()) {
                throw new ApplicationExceptions.EntityAlreadyExistsException(
                    String.format("entity with simple name: %s already exists",
                        existingEntities.get(0).getSimpleName()));
            }

            log.info("Updating entity {} with values data: {}", entity.getUuid(), entityDto.getData());
            entity.update(entityDto);
            entityRepository.save(entity);
        } else {
            throw new ApplicationExceptions.EntityNotFoundException(
                "Entity %s not found for entity list: %s, cannot update! ", entityDto.getUuid(), listName);
        }
    }

    public void deleteEntity(String listName, String entityUUID) {
        String entityListUUID = entityRepository.findEntityListUUIDBySimpleName(listName);

        Entity entity = entityRepository.findByUuid(UUID.fromString(entityUUID));

        if (StringUtils.isNotEmpty(entityListUUID) && entity.getEntityListUUID().equals(
            UUID.fromString(entityListUUID))) {
            entityRepository.delete(entity);
        } else {
            throw new ApplicationExceptions.EntityNotFoundException(
                "Entity %s not found for entity list: %s, cannot delete! ", entityUUID, listName);
        }
    }

}
