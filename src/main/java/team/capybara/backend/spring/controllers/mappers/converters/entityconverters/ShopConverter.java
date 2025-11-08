package team.capybara.backend.spring.controllers.mappers.converters.entityconverters;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import team.capybara.backend.spring.controllers.mappers.converters.EntityIdConverter;
import team.capybara.backend.spring.controllers.repositories.ShopRepository;
import team.capybara.backend.spring.entities.Shop;

import java.util.Optional;
import java.util.UUID;

@Component
public final class ShopConverter implements EntityIdConverter<Shop> {
    private final ShopRepository shopRepository;

    public ShopConverter(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;
    }

    @Override
    public Shop toEntity(UUID id) {
        Optional<Shop> shop = shopRepository.findById(id);

        if (shop.isEmpty()) {
            throw new EntityNotFoundException("Shop not found; id=" + id);
        }

        return shop.get();
    }
}
