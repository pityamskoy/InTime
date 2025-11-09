package team.capybara.backend.spring.controllers.mappers.converters;

import jakarta.persistence.EntityNotFoundException;
import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.entities.EntityWithId;

import java.util.UUID;

/**
 * Implementations of {@code EntityConverter} suppose to throw {@link EntityNotFoundException},
 * which should be caught and be covered with new {@link EntityNotFoundException} in {@link Mapper}
 *
 * @param <Entity> any entity, which implements {@link EntityWithId}
 */

public interface EntityIdConverter<Entity> {

    /**
     * @param id the id of an {@link EntityWithId} object
     * @return {@link EntityWithId}
     */
    Entity toEntity(UUID id);
}
