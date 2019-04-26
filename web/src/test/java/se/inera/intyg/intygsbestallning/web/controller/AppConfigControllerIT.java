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

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import org.junit.jupiter.api.Test;

import com.jayway.restassured.RestAssured;

import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;

public class AppConfigControllerIT extends BaseRestIntegrationTest {


    @Test
    public void testGetAppConfig() {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(OK).when().get(AppConfigController.APPCONFIG_REQUEST_MAPPING).then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-appconfig-response-schema.json"));
    }

}