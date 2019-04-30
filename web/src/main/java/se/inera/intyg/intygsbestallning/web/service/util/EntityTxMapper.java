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

package se.inera.intyg.intygsbestallning.web.service.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
import javax.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class EntityTxMapper {

    static final Logger LOG = LoggerFactory.getLogger(EntityTxMapper.class);

    public static final Map<String, String> OK = Collections.singletonMap("status", "ok");

    private ObjectMapper mapper;

    public EntityTxMapper(@Qualifier("customObjectMapper") ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    public <E> ResponseEntity<String> jsonResponseEntity(final Supplier<E> supplier) {
        return ResponseEntity.ok(map(supplier.get()));
    }

    public <E> Response jsonResponse(final Supplier<E> supplier) {
        return Response.ok(unmap(map(supplier.get()))).build();
    }

    // maps to json string
    <E> String map(final E entity) {
        try {
            final String json = mapper.writeValueAsString(entity);
            LOG.debug("{}", json);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    // maps json to map
    Map<String, String> unmap(final String json) {
        try {
            return mapper.readValue(json, Map.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
