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
import org.junit.jupiter.api.Test;
import com.jayway.restassured.RestAssured;
import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;

class MailLinkControllerIT extends BaseRestIntegrationTest {
    private static final String MAILLINK_API_ENDPOINT = "/maillink/";

    @Test
    void testMaillinkRedirectsCorrectly() {
        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        given().redirects().follow(false).expect().statusCode(FOUND).when().get(MAILLINK_API_ENDPOINT + id)
                .then().assertThat().header("Location", RestAssured.baseURI + "/#/bestallning/" + id);

        deleteBestallning(id);
    }

    @Test
    void testMaillinkWithoutCorrectAuth() {
        Integer id = createBestallning(loadJson("integrationtests/bestallning_other_careunit.json"));

        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        given().redirects().follow(false).expect().statusCode(FOUND)
                .when().get(MAILLINK_API_ENDPOINT + id)
                .then().assertThat().header("Location", RestAssured.baseURI + "/#/exit/LOGIN_FEL002");

        deleteBestallning(id);
    }

/*    @Test
    void testMaillinkWithoutAuthRedirectsToLogin() {
        Integer id = createBestallning(loadJson("integrationtests/bestallning.json"));

        given().redirects().follow(false).expect().statusCode(FOUND).when().get(MAILLINK_API_ENDPOINT + id)
                .then().assertThat().header("Location", RestAssured.baseURI + "/welcome.html");

        deleteBestallning(id);
    }*/

    @Test
    void testMaillinkRedirectToUnknownBestallning() {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);

        given().redirects().follow(false).expect().statusCode(FOUND).when().get(MAILLINK_API_ENDPOINT + 1337)
                .then().assertThat().header("Location", RestAssured.baseURI + "/#/exit/NOT_FOUND");
    }

}
