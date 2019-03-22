package se.inera.intyg.intygsbestallning.persistence.service;

import com.google.common.base.Charsets;
import io.github.benas.randombeans.EnhancedRandomBuilder;
import io.github.benas.randombeans.api.EnhancedRandom;


import static io.github.benas.randombeans.FieldPredicates.named;
import static io.github.benas.randombeans.FieldPredicates.ofType;

public abstract class TestSupport {
    public static EnhancedRandom random = EnhancedRandomBuilder.aNewEnhancedRandomBuilder()
            .randomizationDepth(10)
            .charset(Charsets.UTF_8)
            .excludeField(named("id").and(ofType(Long.class)))
            .scanClasspathForConcreteTypes(true)
            .build();

    public <T> T randomize(final Class<T> type, final String... excludedFields) {
        return random.nextObject(type, excludedFields);
    }
}
