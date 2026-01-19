package team.capybara.backend.spring.controllers.dto.other.filters;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public final class FeedFilterEntity {
    private Integer limit;
    private String name;
    private Boolean isOnlyFreeProducts;
    private List<String> shopsId;
    private List<String> categoriesId;
    private Double distance;
    private Double userLat;
    private Double userLon;

    @SuppressWarnings(value = {"unused"})
    public FeedFilterEntity(
            @Nullable Integer limit,
            @Nullable String name,
            @Nullable Boolean isOnlyFreeProducts,
            @Nullable List<String> shopsId,
            @Nullable List<String> categoriesId,
            @Nullable Double Distance,
            @Nullable Double userLat,
            @Nullable Double userLon
    ) {
        makeAllEmptyFieldsEquivalentToNull(limit, name, isOnlyFreeProducts, shopsId, categoriesId, Distance, userLat, userLon);
    }

    @SuppressWarnings(value = {"unused", "DataFlowIssue"})
    public void makeAllEmptyFieldsEquivalentToNull (
            @Nullable Integer limit,
            @Nullable String name,
            @Nullable Boolean isOnlyFreeProducts,
            @Nullable List<String> shopsId,
            @Nullable List<String> categoriesId,
            @Nullable Double Distance,
            @Nullable Double userLat,
            @Nullable Double userLon
    ) {
        try {
            if (name.isEmpty()) {
                name = null;
            }
        } catch (NullPointerException _) {}
        try {
            if (shopsId.isEmpty()) {
                shopsId = null;
            }
        } catch (NullPointerException _) {}
        try {
            if (categoriesId.isEmpty()) {
                categoriesId = null;
            }
        } catch (NullPointerException _) {}

        this.limit = Objects.requireNonNullElse(limit, 30);
        this.name = name;
        this.isOnlyFreeProducts = isOnlyFreeProducts;
        this.shopsId = shopsId;
        this.categoriesId = categoriesId;
        this.distance = Distance;
        this.userLat = userLat;
        this.userLon = userLon;
    }
}
