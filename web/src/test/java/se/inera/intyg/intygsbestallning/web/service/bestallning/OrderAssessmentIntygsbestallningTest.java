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

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import io.vavr.control.Try;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.util.ReflectionUtils;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.v1.ErrorIdType;
import se.riv.intygsbestallning.certificate.order.v1.ResultCodeType;
import java.util.List;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationErrorCode;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class OrderAssessmentIntygsbestallningTest {

    //CHECKSTYLE:OFF MethodName

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
        when(bestallningTextService.isIntygTypValid(eq(type.getCertificateType().getCode())))
                .thenReturn(true);

        when(createBestallningService.create(any(CreateBestallningRequest.class))).thenReturn(1L);

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).isIntygTypValid(type.getCertificateType().getCode());
        verify(createBestallningService, times(1)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getAssessmentId().getExtension()).isEqualTo("1");
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.OK);

    }

    @Test
    void test_GTA_FEL01_IncorrectPersonnummerRoot() {
        var type = getXmlRequestAndMapToObject("gta_fel01_personnummer");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(type.getCitizen().getPersonalIdentity().getRoot()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).isIntygTypValid(type.getCertificateType().getCode());
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
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(type.getCareUnitId().getRoot()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).isIntygTypValid(type.getCertificateType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.VALIDATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    @Test
    void test_GTA_FEL01_IncorrectCertificateTypeCodeSystem() {
        var type = getXmlRequestAndMapToObject("gta_fel01_intygtyp");
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(type.getCertificateType().getCodeSystem()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).isIntygTypValid(type.getCertificateType().getCode());
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

        when(bestallningTextService.isIntygTypValid(eq(type.getCertificateType().getCode())))
                .thenReturn(true);

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01,
                List.of(type.getAuthorityAdministrativeOfficial().getAuthority().getCodeSystem()));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).isIntygTypValid(type.getCertificateType().getCode());
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

        when(bestallningTextService.isIntygTypValid(eq(type.getCertificateType().getCode())))
                .thenReturn(true);

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL02,
                List.of(type.getAuthorityAdministrativeOfficial().getAuthority().getCode()));

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).isIntygTypValid(type.getCertificateType().getCode());
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
        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL05, List.of(type.getCitizen().getPersonalIdentity().getExtension()));
        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(0)).isIntygTypValid(type.getCertificateType().getCode());
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
    void test_GTA_FEL08_IncorrectCertificateType() {
        var type = getXmlRequestAndMapToObject("gta_fel08_intygtyp");

        when(bestallningTextService.isIntygTypValid(eq(type.getCertificateType().getCode()))).thenReturn(false);

        var expectedError = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL08, List.of());

        var response = orderAssessment.orderAssessment(LOGICAL_ADDRESS, type);

        verify(bestallningTextService, times(1)).isIntygTypValid(type.getCertificateType().getCode());
        verify(createBestallningService, times(0)).create(any(CreateBestallningRequest.class));

        assertThat(response).isNotNull();
        assertThat(response.getResult()).isNotNull();
        assertThat(response.getResult().getResultCode()).isEqualTo(ResultCodeType.ERROR);
        assertThat(response.getResult().getErrorId().get(0)).isEqualTo(ErrorIdType.APPLICATION_ERROR);
        assertThat(response.getResult().getResultText()).isEqualTo(expectedError.getMessage());
    }

    //CHECKSTYLE:ON MethodName
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
}
