package team.capybara.backend.spring.controllers.filters;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

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

    @SuppressWarnings(value = {"unused", "DataFlowIssue"})
    public FeedFilterEntity (
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

        this.name = name;
        this.isOnlyFreeProducts = isOnlyFreeProducts;
        this.shopsId = shopsId;
        this.categoriesId = categoriesId;
        this.distance = Distance;
        this.userId = userId;
    }
}
