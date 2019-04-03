package se.inera.intyg.intygsbestallning.web.service.util;

import io.vavr.control.Try;
import org.jetbrains.annotations.NotNull;
import se.inera.intyg.intygsbestallning.common.dto.SimpleBestallningRequest;

public final class BestallningUtil {
    @NotNull
    public static Try<Long> resolveId(String requestId) {
        var id = Try.of(() -> Long.valueOf(requestId));
        if (id.isFailure()) {
            throw new IllegalArgumentException("id must be of typ Long");
        }
        return id;
    }
}
