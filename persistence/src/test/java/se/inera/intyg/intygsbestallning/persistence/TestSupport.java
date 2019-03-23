package se.inera.intyg.intygsbestallning.persistence;

import com.google.common.base.Charsets;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;
import io.github.benas.randombeans.api.Randomizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import se.inera.intyg.schemas.contract.Personnummer;


import static io.github.benas.randombeans.FieldPredicates.named;
import static io.github.benas.randombeans.FieldPredicates.ofType;

public abstract class TestSupport {

    private DateTimeFormatter idFormatter = DateTimeFormatter.ofPattern("yyyyMMddmmss");

    private EnhancedRandom enhancedRandom = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .randomizationDepth(10)
            .collectionSizeRange(1, 20)
            .charset(Charsets.UTF_8)
            .excludeField(named("id").and(ofType(Long.class)))
            .randomize(ofType(Personnummer.class), idRandomizer())
            .scanClasspathForConcreteTypes(true)
            .build();

    protected EnhancedRandom randomizer() {
        return enhancedRandom;
    }

    protected Randomizer<Personnummer> idRandomizer() {
        return () -> {
            LocalDateTime day = LocalDateTime.now().minusMonths(enhancedRandom.nextInt(12 * 75));
            return Personnummer.createPersonnummer(day.format(idFormatter)).get();
        };
    }

    protected <T> T randomize(final Class<T> type, final String... excludedFields) {
        return enhancedRandom.nextObject(type, excludedFields);
    }
}
