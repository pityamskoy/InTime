package team.capybara.backend.spring.controllers.controllers.pagination;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public final class PaginationHandler<Objects> {
    public List<Objects> makeSliceFromList(List<Objects> objectsToMakeSlice, int offset, int limit) {
        List<Objects> slice = new ArrayList<>();

        if (!objectsToMakeSlice.isEmpty()) {
            try {
                slice = objectsToMakeSlice.subList(limit * (offset - 1), limit * (offset));
            } catch (IndexOutOfBoundsException _) {
                if (limit * (offset - 1) == objectsToMakeSlice.size()) {
                    slice.add(objectsToMakeSlice.get(limit * (offset - 1)));
                } else if (limit * (offset - 1) < objectsToMakeSlice.size()) {
                    slice = objectsToMakeSlice.subList(limit * (offset - 1), objectsToMakeSlice.size());
                }
            }
        }

        return slice;
    }
}
