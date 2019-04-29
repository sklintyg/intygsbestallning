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
import static org.hamcrest.core.Is.is;

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
        given().expect().statusCode(SERVER_ERROR).when().get(BESTALLNINGAR_API_ENDPOINT + "/0").then()
            .body("errorCode", is("NOT_FOUND"));
    }

    @Test
    public void testGetBestallningOrgIdMmismatch()
    {
        Integer id = createBestallning(loadJson("integrationtests/bestallning_other_orgid.json"));

        RestAssured.sessionId = getAuthSession(DEFAULT_USER);
        given().expect().statusCode(SERVER_ERROR).when().get(BESTALLNINGAR_API_ENDPOINT + "/" + id).then()
                .body("errorCode", is("VARDGIVARE_ORGNR_MISMATCH"));

        deleteBestallning(id);
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
