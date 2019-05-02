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
package se.inera.intyg.intygsbestallning.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;
import se.inera.intyg.intygsbestallning.web.bestallning.AccepteraBestallning;
import se.inera.intyg.intygsbestallning.web.bestallning.AvvisaBestallning;
import se.inera.intyg.intygsbestallning.web.bestallning.RaderaBestallning;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

public class BestallningControllerIT extends BaseRestIntegrationTest {

    private static final String BESTALLNINGAR_API_ENDPOINT = "/api/bestallningar";

    @Test
    public void testGetBestallningar()
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(OK).when().get(BESTALLNINGAR_API_ENDPOINT).then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-bestallningar-response-schema.json"));
    }

    @Test
    public void testGetBestallning()
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().expect().statusCode(OK).when().get(BESTALLNINGAR_API_ENDPOINT + "/" + id).then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-bestallning-response-schema.json"));

        deleteBestallning(id);
    }

    @Test
    public void testGetBestallningNotFound()
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(NOT_FOUND).when().get(BESTALLNINGAR_API_ENDPOINT + "/0");
    }

    @Test
    public void testAccepteraBestallning() throws JsonProcessingException
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().contentType(APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(new AccepteraBestallning("Fritext")))
                .expect().statusCode(OK).when().post(BESTALLNINGAR_API_ENDPOINT + "/" + id + "/acceptera").then();

        deleteBestallning(id);
    }

    @Test
    public void testAvvisaBestallning() throws JsonProcessingException
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().contentType(APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(new AvvisaBestallning("Fritext")))
                .expect().statusCode(OK).when().post(BESTALLNINGAR_API_ENDPOINT + "/" + id + "/avvisa").then();

        deleteBestallning(id);
    }

    @Test
    public void testRaderaBestallning() throws JsonProcessingException
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().contentType(APPLICATION_JSON)
                .body(objectMapper.writeValueAsString(new RaderaBestallning("Fritext")))
                .expect().statusCode(OK).when().post(BESTALLNINGAR_API_ENDPOINT + "/" + id + "/radera").then();
    }

    @Test
    public void testKlarmarkeraBestallning()
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning_accepterad.json"));

        given().contentType(APPLICATION_JSON)
                .expect().statusCode(OK).when().post(BESTALLNINGAR_API_ENDPOINT + "/" + id + "/klarmarkera").then();

        deleteBestallning(id);
    }

    @Test
    public void testPdfForfragan()
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().expect().statusCode(OK).when().get(BESTALLNINGAR_API_ENDPOINT + "/" + id + "/pdf/forfragan").then();

        deleteBestallning(id);
    }

    @Test
    public void testPdfFaktureringsunderlag()
    {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().expect().statusCode(OK).when().get(BESTALLNINGAR_API_ENDPOINT + "/" + id + "/pdf/faktureringsunderlag").then();

        deleteBestallning(id);
    }
}
