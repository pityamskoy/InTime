package team.capybara.backend.spring.controllers.dto.other.pagination;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
public final class PaginationLimit {
    private Integer limit;

    public PaginationLimit(@Nullable Integer limit) {
        this.limit = Objects.requireNonNullElse(limit, 30);
    }
}
