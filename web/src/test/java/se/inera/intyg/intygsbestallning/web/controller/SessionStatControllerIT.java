package se.inera.intyg.intygsbestallning.web.controller;

import com.jayway.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class SessionStatControllerIT extends BaseRestIntegrationTest {

    private static final String SESSION_STAT_API_ENDPOINT =  "/public-api/session-stat/ping";

    @Test
    public void testGetStats() {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(OK).when().get(SESSION_STAT_API_ENDPOINT).then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-stat-response-schema.json"));
    }

}
