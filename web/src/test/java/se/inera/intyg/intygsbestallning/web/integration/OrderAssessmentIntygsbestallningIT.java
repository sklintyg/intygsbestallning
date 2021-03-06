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
package se.inera.intyg.intygsbestallning.web.integration;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.ResponseBodyExtractionOptions;
import se.inera.intyg.intygsbestallning.web.BaseRestIntegrationTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class OrderAssessmentIntygsbestallningIT extends BaseRestIntegrationTest {

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
                        "F1.0_AF00213", "Detta är en kommentar",
                        "Bli frisk", "Bowla",
                        "Hanna Handläggare", "123-123123", "handlaggare@ineratestar.se",
                        "191212121212", "Tolvan", "Tolvansson", "Rullator",
                        "Kommer från Tolvmåla"));


        ResponseBodyExtractionOptions body = given()
                .body(requestTemplate.render()).when()
                .post(ORDER_ASSESSMENT_ENDPOINT).then()
                .statusCode(200).rootPath(RESPONSE_BASE)
                .body("result.resultCode", is("OK"))
                .body("assessmentId.extension", Matchers.notNullValue())
                .extract();

        deleteBestallning(body.xmlPath().setRootPath(RESPONSE_BASE).getInt( "assessmentId.extension"));
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
                .body("result.errorId", is("APPLICATION_ERROR"));
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
