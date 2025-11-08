package team.capybara.backend.spring.controllers.mappers;


import java.util.UUID;

public interface Mapper<EntityObject, DtoObject> {
    DtoObject getEntity(EntityObject entityObject);

    DtoObject postEntity(DtoObject dtoObject);

    DtoObject putEntity(DtoObject dtoObject);

    void deleteEntity(UUID entityId);
}
