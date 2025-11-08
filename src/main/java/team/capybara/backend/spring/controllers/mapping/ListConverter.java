package team.capybara.backend.spring.controllers.mapping;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.entities.EntityWithId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public final class ListConverter {
    public static List<UUID> toIdList(List<EntityWithId> entities) {
        List<UUID> entitiesId = new ArrayList<>();

        for (EntityWithId entity : entities) {
            entitiesId.add(entity.getId());
        }

        return entitiesId;
    }

    public static List<EntityWithId> toEntityList(List<UUID> entitiesId, JpaRepository<EntityWithId, UUID> repository) {
        List<EntityWithId> entities = new ArrayList<>();

        for (UUID id : entitiesId) {
            Optional<EntityWithId> entityWithId = repository.findById(id);

            if (entityWithId.isEmpty()) {
                String entityName = entityWithId.getClass().getName();
                throw new EntityNotFoundException(entityName + " not found; id=" + id);
            }
            entities.add(entityWithId.get());
        }

        return entities;
    }
}
