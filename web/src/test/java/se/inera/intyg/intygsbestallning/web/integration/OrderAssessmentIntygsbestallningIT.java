package se.inera.intyg.intygsbestallning.web.integration;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class OrderAssessmentIntygsbestallningIT {

    private static final String ORDER_ASSESSMENT_ENDPOINT = "/services/order-assessment-responder";
    private static final String RESPONSE_BASE = "Envelope.Body.OrderAssessmentResponse.";

    private ST requestTemplate;

    private STGroup templateGroup;

    @BeforeEach
    public void setupTestSpecific() {
        RestAssured.requestSpecification = new RequestSpecBuilder().setContentType("application/xml;charset=utf-8").build();

        templateGroup = new STGroupFile("integrationtests/order_assessment.stg");
        requestTemplate = templateGroup.getInstanceOf("request");
    }

    @Test
    public void testOrderAssessment() {

        requestTemplate.add("data",
                new OrderAssessmentIntygsbestallningIT.RequestOrderAssessment(
                        "IFV1239877878-1042",
                        "AF00213", "Detta är en kommentar",
                        "Bli frisk", "Bowla",
                        "Hanna Handläggare", "123-123123", "handlaggare@ineratestar.se",
                        "191212121212", "Tolvan", "Tolvansson", "Rullator",
                        "Kommer från Tolvmåla"));

        given().body(requestTemplate.render()).when().post(ORDER_ASSESSMENT_ENDPOINT).then()
                .statusCode(200).rootPath(RESPONSE_BASE)
                .body("result.resultCode", is("OK"))
                .body("assessmentId.extension", Matchers.notNullValue());
    }

    @Test
    public void testOrderAssessmentError() {

        requestTemplate.add("data",
                new OrderAssessmentIntygsbestallningIT.RequestOrderAssessment(
                        "IFV1239877878-1042",
                        "", "Detta är en kommentar",
                        "Bli frisk", "Bowla",
                        "Hanna Handläggare", "123-123123", "handlaggare@ineratestar.se",
                        "191212121212", "Tolvan", "Tolvansson", "Rullator",
                        "Kommer från Tolvmåla"));

        given().body(requestTemplate.render()).when().post(ORDER_ASSESSMENT_ENDPOINT).then()
                .statusCode(200).rootPath(RESPONSE_BASE)
                .body("result.resultCode", is("ERROR"))
                .body("result.errorId", is("APPLICATION_ERROR"))
                .body("assessmentId.extension", is(""));
    }

    private static class RequestOrderAssessment {
        public final String vardenhetHsaId;
        public final String utredningTyp;
        public final String kommentar;
        public final String syfte;
        public final String planeradeAtgarder;
        public final String handlaggareNamn;
        public final String handlaggareTelefon;
        public final String handlaggareEpost;
        public final String patientPersonId;
        public final String patientFornamn;
        public final String patientEfternamn;
        public final String patientBehov;
        public final String patientBakgrund;

        public RequestOrderAssessment(String vardenhetHsaId, String utredningTyp, String kommentar, String syfte, String planeradeAtgarder,
                                      String handlaggareNamn, String handlaggareTelefon, String handlaggareEpost, String patientPersonId,
                                      String patientFornamn, String patientEfternamn, String patientBehov, String patientBakgrund) {
            this.vardenhetHsaId = vardenhetHsaId;
            this.utredningTyp = utredningTyp;
            this.kommentar = kommentar;
            this.syfte = syfte;
            this.planeradeAtgarder = planeradeAtgarder;
            this.handlaggareNamn = handlaggareNamn;
            this.handlaggareTelefon = handlaggareTelefon;
            this.handlaggareEpost = handlaggareEpost;
            this.patientPersonId = patientPersonId;
            this.patientFornamn = patientFornamn;
            this.patientEfternamn = patientEfternamn;
            this.patientBehov = patientBehov;
            this.patientBakgrund = patientBakgrund;
        }
    }
}
