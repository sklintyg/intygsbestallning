package se.inera.intyg.intygsbestallning.web.controller;

import com.jayway.restassured.RestAssured;
import org.junit.jupiter.api.Test;
import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserControllerIT extends BaseRestIntegrationTest {

    private static final String ANVANDARE_API_ENDPOINT =  "/api/anvandare";

    @Test
    public void testGetUser() {
        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(OK).when().get(ANVANDARE_API_ENDPOINT).then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-user-response-schema.json"));
    }

    @Test
    public void testChangeUnit() {
        RestAssured.sessionId = getAuthSession(MULTI_VARDENHET_USER);
        given().expect().statusCode(OK).when().post(ANVANDARE_API_ENDPOINT + "/unit-context/IFV1239877878-1045").then()
                .body(matchesJsonSchemaInClasspath("jsonschema/get-user-response-schema.json"));

    }

    @Test
    public void testChangeUnitNotAuthhorized() {
        RestAssured.sessionId = getAuthSession(MULTI_VARDENHET_USER);
        given().expect().statusCode(FORBIDDEN).when().post(ANVANDARE_API_ENDPOINT + "/unit-context/111");

    }
}
