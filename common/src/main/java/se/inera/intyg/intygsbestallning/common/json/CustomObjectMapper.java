/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package se.inera.intyg.intygsbestallning.common.json;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.kotlin.KotlinModule;

public class CustomObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    public CustomObjectMapper() {
        setSerializationInclusion(JsonInclude.Include.ALWAYS);

        configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        registerModule(new JaxbAnnotationModule());  // support the standard JAXB annotations
        registerModule(new LocalDateTimeSezializerModule());
        registerModule(new Jdk8Module());
        registerModule(new KotlinModule());

        setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
    }

    private static final class LocalDateTimeSezializerModule extends SimpleModule {

        private static final long serialVersionUID = 1L;

        private LocalDateTimeSezializerModule() {
            addSerializer(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE);
            addDeserializer(LocalDateTime.class, LocalDateTimeDeserializer.INSTANCE);

            addSerializer(LocalDate.class, LocalDateSerializer.INSTANCE);
            addDeserializer(LocalDate.class, LocalDateDeserializer.INSTANCE);

            addSerializer(LocalTime.class, LocalTimeSerializer.INSTANCE);
            addDeserializer(LocalTime.class, LocalTimeDeserializer.INSTANCE);
        }
    }
}
