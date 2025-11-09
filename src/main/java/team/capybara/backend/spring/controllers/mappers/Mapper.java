package team.capybara.backend.spring.controllers.mappers;


import java.util.UUID;

public interface Mapper<Entity, EntityDto> {
    EntityDto getEntity(Entity entity);

    EntityDto postEntity(EntityDto entityDtoToCreate);

    EntityDto putEntity(EntityDto entityDtoToUpdate);

    void deleteEntity(UUID entityId);
}
