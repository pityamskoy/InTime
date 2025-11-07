package team.capybara.backend.spring.controllers.mapping;


import java.util.UUID;

public interface Mapper<EntityObject, DtoObject> {
    DtoObject getEntity(EntityObject entityObject);

    EntityObject postEntity(DtoObject dtoObject);

    EntityObject putEntity(DtoObject dtoObject);

    void deleteEntity(UUID entityId);
}
