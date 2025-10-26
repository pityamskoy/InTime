package team.capybara.backend.spring.controllers.mapping;


public interface Mapper<EntityObject, DtoObject> {
    DtoObject toDto(EntityObject entityObject);

    EntityObject postEntity(DtoObject dtoObject);
}
