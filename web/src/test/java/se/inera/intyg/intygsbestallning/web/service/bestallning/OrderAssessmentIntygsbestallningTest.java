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
package se.inera.intyg.intygsbestallning.web.service.bestallning;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_EMAIL;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_FULL_NAME;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_ADDRESS;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CITY;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CODE;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_COST_CENTER;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_NAME;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_PHONE_NUMBER;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_CASE_REFERENCE;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_CITIZEN_SITUATION_BACKGROUND;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_PLANNED_ACTIONS;
import static se.inera.intyg.intygsbestallning.web.service.bestallning.OrderAssessmentIntygsbestallning.ATTR_PURPOSE;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import io.vavr.control.Try;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationErrorCode;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.text.bestallning.BestallningTexter;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentResponseType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.v1.ErrorIdType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class OrderAssessmentIntygsbestallningTest {

    // CHECKSTYLE:OFF MethodName

    private static final String LOGICAL_ADDRESS = "123456789";
    private static final String SOURCE_SYSTEM_HSA_ID = "9876543421";

    @Mock
    private BestallningTextService bestallningTextService;

    @Mock
    private IntegrationProperties integrationProperties;

    @Mock
    private CreateBestallningService createBestallningService;

    @InjectMocks
    private OrderAssessmentIntygsbestallning orderAssessment;

    @BeforeEach
    void setup() {
        Try.run(() -> ReflectionUtils.setField(IntegrationProperties.class.getField("sourceSystemHsaId"),
                integrationProperties, SOURCE_SYSTEM_HSA_ID));
    }

    @Test
    void testOKRequest() {

        var type = getXmlRequestAndMapToObject("valid");
        var bestallningTexter = buildBestallningTexter(type);

        when(bestallningTextService.getBestallningTexter(eq(type.getOrderFormType().getCode()))).thenReturn(Optional.of(bestallningTexter));

        when(createBestallningService.create(any(CreateBestallningRequest.class))).thenReturn(1L);

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(1)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getAssessmentId().getExtension()).isEqualTo("1");
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.OK);

    }

    @Test
    void test_GTA_FEL01_IncorrectPersonnummerRoot() {
        var type = getXmlRequestAndMapToObject("gta_fel01_personnummer");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01,
                List.of(type.getCitizen().getPersonalIdentity().getRoot()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL01_IncorrectSamordningsnummerRoot() {
        var type = getXmlRequestAndMapToObject("gta_fel01_samordningsnummer");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01,
                List.of(type.getCitizen().getPersonalIdentity().getRoot()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL01_IncorrectHSARoot() {
        var type = getXmlRequestAndMapToObject("gta_fel01_hsa");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01,
                List.of(type.getCareUnitId().getRoot()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL01_IncorrectOrderFormTypeCodeSystem() {
        var type = getXmlRequestAndMapToObject("gta_fel01_intygtyp");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01,
                List.of(type.getOrderFormType().getCodeSystem()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL01_IncorrectAuthorityCodeSystem() {
        var type = getXmlRequestAndMapToObject("gta_fel01_myndighet");
        var bestallningTexter = buildBestallningTexter(type);
        when(bestallningTextService.getBestallningTexter(eq(type.getOrderFormType().getCode())))
                .thenReturn(Optional.of(bestallningTexter));

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01,
                List.of(type.getAuthorityAdministrativeOfficial().getAuthority().getCodeSystem()));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL02_IncorrectAuthority() {
        var type = getXmlRequestAndMapToObject("gta_fel02_myndighet");
        var bestallningTexter = buildBestallningTexter(type);
        when(bestallningTextService.getBestallningTexter(eq(type.getOrderFormType().getCode())))
                .thenReturn(Optional.of(bestallningTexter));

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL02,
                List.of(type.getAuthorityAdministrativeOfficial().getAuthority().getCode()));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL05_IncorrectPersonnummerFormat() {
        var type = getXmlRequestAndMapToObject("gta_fel05_personnummer");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL05,
                List.of(type.getCitizen().getPersonalIdentity().getExtension()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL07_AssessmentIdExists() {
        var type = getXmlRequestAndMapToObject("gta_fel07_bestallningid");

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL07, List.of());
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL08_IncorrectOrderFormType() {
        var type = getXmlRequestAndMapToObject("gta_fel08_intygtyp");

        when(bestallningTextService.getBestallningTexter(eq(type.getOrderFormType().getCode()))).thenReturn(Optional.empty());

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL08, List.of());

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).getBestallningTexter(type.getOrderFormType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.APPLICATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL13_Maxlength_Purpose() {
        var type = getXmlRequestAndMapToObject("gta_fel13_purpose");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13, List.of(ATTR_PURPOSE));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_PlannedActions() {
        var type = getXmlRequestAndMapToObject("gta_fel13_plannedactions");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13, List.of(ATTR_PLANNED_ACTIONS));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_CaseReference() {
        var type = getXmlRequestAndMapToObject("gta_fel13_casereference");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13, List.of(ATTR_CASE_REFERENCE));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_CitizenBackground() {
        var type = getXmlRequestAndMapToObject("gta_fel13_citizenbackground");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_CITIZEN_SITUATION_BACKGROUND));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_OfficeCostcenter() {
        var type = getXmlRequestAndMapToObject("gta_fel13_officecostcenter");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_COST_CENTER));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_Officename() {
        var type = getXmlRequestAndMapToObject("gta_fel13_officename");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_NAME));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_fullname() {
        var type = getXmlRequestAndMapToObject("gta_fel13_fullname");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_FULL_NAME));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_phone() {
        var type = getXmlRequestAndMapToObject("gta_fel13_phone");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_PHONE_NUMBER));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_email() {
        var type = getXmlRequestAndMapToObject("gta_fel13_email");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_EMAIL));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_officepostaddress() {
        var type = getXmlRequestAndMapToObject("gta_fel13_postadress");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_ADDRESS));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_officepostalcode() {
        var type = getXmlRequestAndMapToObject("gta_fel13_postkod");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CODE));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    @Test
    void test_GTA_FEL13_Maxlength_officepostalcity() {
        var type = getXmlRequestAndMapToObject("gta_fel13_city");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13,
                List.of(ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CITY));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        assertGTA13Response(response, expectedError);
    }

    private void assertGTA13Response(OrderAssessmentResponseType response, IbResponderValidationException expectedError) {
        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    // CHECKSTYLE:ON MethodName
    private OrderAssessmentType getXmlRequestAndMapToObject(String fileName) {
        var resource = Resources.getResource("orderAssessment/" + fileName + ".xml");
        var xmlResult = Try.of(() -> Resources.toString(resource, Charsets.UTF_8));

        if (xmlResult.isFailure()) {
            xmlResult.getCause().printStackTrace();
        }

        var objectMapper = new XmlMapper();
        var mappedResult = Try.of(() -> objectMapper.readValue(xmlResult.get(), OrderAssessmentType.class));

        if (mappedResult.isFailure()) {
            mappedResult.getCause().printStackTrace();
        }

        return mappedResult.get();
    }

    private BestallningTexter buildBestallningTexter(OrderAssessmentType type) {

        var bestallningTexter = new BestallningTexter();

        Field typField = ReflectionUtils.findField(BestallningTexter.class, "typ");
        ReflectionUtils.makeAccessible(typField);
        ReflectionUtils.setField(typField, bestallningTexter, type.getOrderFormType().getCode());

        Field intygTypField = ReflectionUtils.findField(BestallningTexter.class, "intygTyp");
        ReflectionUtils.makeAccessible(intygTypField);
        ReflectionUtils.setField(intygTypField, bestallningTexter, "INTYG_TYP");

        Field intygTypBeskrivningField = ReflectionUtils.findField(BestallningTexter.class, "intygTypBeskrivning");
        ReflectionUtils.makeAccessible(intygTypBeskrivningField);
        ReflectionUtils.setField(intygTypBeskrivningField, bestallningTexter, "INTYG_TYP_BESKRIVNING");

        return bestallningTexter;
    }
}
