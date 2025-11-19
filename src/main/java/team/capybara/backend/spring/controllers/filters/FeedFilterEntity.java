package team.capybara.backend.spring.controllers.filters;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class FeedFilterEntity {
    private int offset;
    private int limit;
    private boolean isOnlyFreeProducts;
    private List<String> shopsId;
    private List<String> categoriesId;
    private int distance;
}
