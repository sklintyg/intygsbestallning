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

import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static se.inera.intyg.intygsbestallning.web.controller.UserController.API_ANVANDARE;

public class UserControllerIT extends BaseRestIntegrationTest {


    @Test
    public void testGetUserWithNoSession() {
        given().expect().statusCode(FORBIDDEN).when().get(API_ANVANDARE);
    }

    @Test
    public void testGetUser() {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(OK).when().get(API_ANVANDARE).then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-user-response-schema.json"));
    }

    @Test
    public void testChangeUnit() {
        RestAssured.sessionId = getAuthSession(MULTI_VARDENHET_USER);
        given().expect().statusCode(OK).when().post(API_ANVANDARE + "/unit-context/IFV1239877878-1045").then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-user-response-schema.json"));

    }

    @Test
    public void testChangeUnitNotAuthhorized() {
        RestAssured.sessionId = getAuthSession(MULTI_VARDENHET_USER);
        given().expect().statusCode(FORBIDDEN).when().post(API_ANVANDARE + "/unit-context/111");

    }
}
