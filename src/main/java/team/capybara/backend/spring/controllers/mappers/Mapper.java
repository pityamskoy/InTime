package team.capybara.backend.spring.controllers.mappers;


import java.util.UUID;

public interface Mapper<Entity, EntityDtoWithId, EntityDtoWithoutId> {
    EntityDtoWithId getEntity(Entity entity);

    EntityDtoWithId postEntity(EntityDtoWithoutId entityDtoToCreate);

    EntityDtoWithId putEntity(EntityDtoWithId entityDtoToUpdate);

    void deleteEntity(UUID entityId);
}
