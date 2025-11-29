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
    @NonNull
    private Integer limit;
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

    @SuppressWarnings(value = {"unused", "DataFlowIssue"})
    public FeedFilterEntity (
            @Nullable Integer limit,
            @Nullable String name,
            @Nullable Boolean isOnlyFreeProducts,
            @Nullable List<String> shopsId,
            @Nullable List<String> categoriesId,
            @Nullable Double Distance,
            @Nullable String userId
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

        this.limit = Objects.requireNonNullElse(limit, 30);
        this.name = name;
        this.isOnlyFreeProducts = isOnlyFreeProducts;
        this.shopsId = shopsId;
        this.categoriesId = categoriesId;
        this.distance = Distance;
        this.userId = userId;
    }
}
