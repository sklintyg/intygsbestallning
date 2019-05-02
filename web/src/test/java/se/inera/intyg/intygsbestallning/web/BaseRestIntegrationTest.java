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
package se.inera.intyg.intygsbestallning.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBodyExtractionOptions;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import se.inera.intyg.intygsbestallning.common.json.CustomObjectMapper;
import se.inera.intyg.intygsbestallning.web.auth.fake.FakeCredentials;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;

public abstract class BaseRestIntegrationTest {
    private static final Logger LOG = LoggerFactory.getLogger(BaseRestIntegrationTest.class);

    private static final String USER_JSON_FORM_PARAMETER = "userJsonDisplay";
    private static final String FAKE_LOGIN_URI = "/fake";

    protected static final FakeCredentials DEFAULT_USER = new FakeCredentials.FakeCredentialsBuilder(
            "ib-user-1", "IFV1239877878-1042")
            .build();

    protected static final FakeCredentials MULTI_VARDENHET_USER = new FakeCredentials.FakeCredentialsBuilder(
            "ib-user-3", "IFV1239877878-1042")
            .build();

    protected CustomObjectMapper objectMapper = new CustomObjectMapper();

    public static final int OK = HttpStatus.OK.value();
    public static final int NOT_FOUND = HttpStatus.NOT_FOUND.value();
    public static final int SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
    public static final int FORBIDDEN = HttpStatus.FORBIDDEN.value();


    /**
     * Common setup for all tests.
     */
    @BeforeEach
    public void setup() {
        RestAssured.reset();
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.baseURI = System.getProperty("integration.tests.baseUrl", "http://localhost:8080");
    }

    /**
     * Log in to Intygsbestallning using the supplied FakeCredentials.
     *
     * @param fakeCredentials
     *            who to log in as
     * @return sessionId for the now authorized user session
     */
    protected String getAuthSession(FakeCredentials fakeCredentials) {
        String credentialsJson;
        try {
            credentialsJson = objectMapper.writeValueAsString(fakeCredentials);
            return getAuthSession(credentialsJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    protected String loadJson(String filePath) {
        ClassPathResource cpr = new ClassPathResource(filePath);
        try {
            return IOUtils.toString(cpr.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected Integer createBestallning(String json) {
        ResponseBodyExtractionOptions body = given().body(json).when().contentType("application/json")
                .post("/api/test/bestallningar").then()
                .statusCode(200).extract().body();
        return body.jsonPath().get("entity.id");
    }

    protected void deleteBestallning(Integer id) {
        given().when()
                .delete("/api/test/bestallningar/" + id)
                .then().statusCode(200);
    }


    private String getAuthSession(String credentialsJson) {
        Response response = given().contentType(ContentType.URLENC).and().redirects().follow(false).and()
                .formParam(USER_JSON_FORM_PARAMETER, credentialsJson).expect()
                .statusCode(HttpServletResponse.SC_FOUND).when()
                .post(FAKE_LOGIN_URI).then().extract().response();

        assertNotNull(response.sessionId());
        return response.sessionId();
    }
}
