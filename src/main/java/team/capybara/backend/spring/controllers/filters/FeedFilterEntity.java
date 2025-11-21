package team.capybara.backend.spring.controllers.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class FeedFilterEntity {
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
}
