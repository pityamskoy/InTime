package team.capybara.backend.spring.controllers.mappers.converters;

import team.capybara.backend.spring.controllers.mappers.Mapper;
import team.capybara.backend.spring.entities.EntityWithId;

import java.util.List;
import java.util.UUID;

/**
 * Implementations of {@code EntityListConverter} suppose to throw {@code EntityNotFoundException},
 * which should be caught and be covered with new {@code EntityNotFoundException} in {@link Mapper}
 *
 * @param <Entity> any entity, which implements {@link EntityWithId}
 */
public interface EntityListIdConverter<Entity> extends EntityIdConverter<Entity> {

    /**
     * This method doesn't suppose to throw any exceptions, and it shouldn't be covered
     * in new {@code EntityNotFoundException} in {@link Mapper}
     *
     * @param entities the {@code List} of {@code entities}
     * @return {@code List}<{@code UUID}> of {@code entities}
     */
    List<UUID> toIdList(List<Entity> entities);

    /**
     *
     * @param entitiesId the {@code List}<{@code UUID}> of {@code entities}
     * @return {@code List} of all entities, which have been found by provided {@code entitiesId}
     */
    List<Entity> toEntityList(List<UUID> entitiesId);
}
