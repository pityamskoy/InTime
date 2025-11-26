package team.capybara.backend.spring.controllers.dto.other.filters;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public final class FeedFilterEntity {
    @Nullable
    private String name;
    @Nullable
    private Boolean isOnlyFreeProducts;
    @Nullable
    private List<String> shopsId;
    @Nullable
    private List<String> categoriesId;
    @Nullable
    private Double distance;
    @Nullable
    private String userId;
    @NonNull
    private Integer limit;

    @SuppressWarnings(value = {"unused", "DataFlowIssue"})
    public FeedFilterEntity (
            @Nullable String name,
            @Nullable Boolean isOnlyFreeProducts,
            @Nullable List<String> shopsId,
            @Nullable List<String> categoriesId,
            @Nullable Double Distance,
            @Nullable String userId,
            @Nullable Integer limit
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
        try {
            if (userId.isEmpty()) {
                userId = null;
            }
        } catch (NullPointerException _) {}

        this.name = name;
        this.isOnlyFreeProducts = isOnlyFreeProducts;
        this.shopsId = shopsId;
        this.categoriesId = categoriesId;
        this.distance = Distance;
        this.userId = userId;
        this.limit = Objects.requireNonNullElse(limit, 30);
    }
}
