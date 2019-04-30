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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static se.inera.intyg.schemas.contract.Personnummer.createPersonnummer;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Optional;
import se.inera.intyg.infra.integration.hsa.exception.HsaServiceCallException;
import se.inera.intyg.infra.integration.hsa.model.Vardgivare;
import se.inera.intyg.infra.integration.hsa.services.HsaOrganizationsService;
import se.inera.intyg.infra.integration.pu.model.Person;
import se.inera.intyg.intygsbestallning.common.domain.Bestallning;
import se.inera.intyg.intygsbestallning.common.domain.Handlaggare;
import se.inera.intyg.intygsbestallning.common.domain.Invanare;
import se.inera.intyg.intygsbestallning.common.domain.Vardenhet;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestHandlaggare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestInvanare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestKontor;
import se.inera.intyg.intygsbestallning.common.exception.IbErrorCodeEnum;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationErrorCode;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;
import se.inera.intyg.intygsbestallning.common.exception.IbServiceException;
import se.inera.intyg.intygsbestallning.common.resolver.BestallningStatusResolver;
import se.inera.intyg.intygsbestallning.common.service.notifiering.NotifieringSendService;
import se.inera.intyg.intygsbestallning.integration.pu.PatientService;
import se.inera.intyg.intygsbestallning.persistence.service.BestallningPersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.InvanarePersistenceService;
import se.inera.intyg.intygsbestallning.persistence.service.VardenhetPersistenceService;
import se.inera.intyg.schemas.contract.Personnummer;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class CreateBestallningServiceImplTest {

    //CHECKSTYLE:OFF MethodName

    private static final String HSA_ID = "12345";
    private static final String ORGNUMMER = "orgnummer";

    @Mock
    private BestallningPersistenceService bestallningPersistenceService;

    @Mock
    private InvanarePersistenceService invanarePersistenceService;

    @Mock
    private BestallningStatusResolver bestallningStatusResolver;

    @Mock
    private VardenhetPersistenceService vardenhetPersistenceService;

    @Mock
    private NotifieringSendService notifieringSendService;

    @Mock
    private PatientService patientService;

    @Mock
    private HsaOrganizationsService hsaOrganizationsService;

    @InjectMocks
    private CreateBestallningServiceImpl createBestallningService;

    @Test
    void testCreateBestallning() {

        var vardgivare = new Vardgivare();
        vardgivare.setId("hsaId");
        vardgivare.setOrgId("orgId");

        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(buildPerson());
        when(hsaOrganizationsService.getVardenhet(any(String.class))).thenReturn(buildVardenhetFromHsa());
        when(hsaOrganizationsService.getVardgivareOfVardenhet(any(String.class))).thenReturn(vardgivare);
        when(vardenhetPersistenceService.getVardenhetByHsaId(any(String.class))).thenReturn(buildVardenhet());
        when(bestallningPersistenceService.saveNewBestallning(any(Bestallning.class))).thenReturn(buildBestallning());

        createBestallningService.create(buildBestallningRequest());
        verify(notifieringSendService, times(1)).nyBestallning(any(Bestallning.class));
    }

    @Test
    void test_GTA_FEL03_TechnicalError_HSA() {

        var expextedException = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL03, List.of());
        var request = buildBestallningRequest();

        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(buildPerson());

        given(hsaOrganizationsService.getVardenhet(anyString())).willAnswer(invocation -> { throw new HsaServiceCallException(); });

        assertThatThrownBy(() -> createBestallningService.create(request)).isEqualTo(expextedException);
    }

    @Test
    void test_GTA_FEL04_Error_HSA() {
        var expextedException = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL04, List.of());
        var request = buildBestallningRequest();

        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(buildPerson());

        given(hsaOrganizationsService.getVardenhet(anyString())).willAnswer(invocation -> { throw new Exception(); });

        assertThatThrownBy(() -> createBestallningService.create(request)).isEqualTo(expextedException);
    }

    @Test
    void test_GTA_FEL06_VardgivareMissingOrgId() {
        var expextedException = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL06, List.of());
        var request = buildBestallningRequest();

        var vardgivare = new Vardgivare();
        vardgivare.setId("hsaId");

        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(buildPerson());
        when(hsaOrganizationsService.getVardenhet(anyString())).thenReturn(buildVardenhetFromHsa());
        when(hsaOrganizationsService.getVardgivareOfVardenhet(anyString())).thenReturn(vardgivare);
        assertThatThrownBy(() -> createBestallningService.create(request)).isEqualTo(expextedException);
    }

    @Test
    void test_GTA_FEL09_VardenhetMissingEmail() {
        var expextedException = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL09, List.of());
        var request = buildBestallningRequest();

        var vardenhet = buildVardenhetWithoutEmail();

        when(patientService.lookupPersonnummerFromPU(any(Personnummer.class))).thenReturn(buildPerson());
        when(hsaOrganizationsService.getVardenhet(anyString())).thenReturn(vardenhet);
        assertThatThrownBy(() -> createBestallningService.create(request)).isEqualTo(expextedException);
    }

    @Test
    void test_GTA_FEL10_PuLookupFailsWithException() {
        var expextedException = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL10, List.of());
        var request = buildBestallningRequest();

        when(patientService.lookupPersonnummerFromPU(eq(request.getInvanare().getPersonnummer())))
                .thenThrow(new IbServiceException(IbErrorCodeEnum.PU_ERROR, "Could not get uppslag from PU"));

        assertThatThrownBy(() -> createBestallningService.create(request)).isEqualTo(expextedException);
    }

    @Test
    void test_GTA_FEL10_PuLookupFailsWithNotFound() {
        var expextedException = new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL11, List.of());
        var request = buildBestallningRequest();

        when(patientService.lookupPersonnummerFromPU(eq(request.getInvanare().getPersonnummer()))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> createBestallningService.create(request)).isEqualTo(expextedException);
    }

    private CreateBestallningRequest buildBestallningRequest() {
        return new CreateBestallningRequest(
                new CreateBestallningRequestInvanare(createPersonnummer("191212121212").get(), "Test"),
                new CreateBestallningRequestHandlaggare("", "", "", "",
                        new CreateBestallningRequestKontor("", "", "", "", "")),
                "syfte", "insatser", "intygsTyp", 1.0, "hsa-id", "referens");
    }

    private Optional<Person> buildPerson() {
        Person person = new Person(createPersonnummer("191212121212").get(),
                false, false, "Tolvan", "", "Tolvansson", "", "", "");
        return Optional.of(person);
    }


    private Optional<Vardenhet> buildVardenhet() {
        Vardenhet vardenhet = new Vardenhet(1L, HSA_ID, "vardgivare-hsaId", ORGNUMMER, "namn", "e@post.com", "standard");
        return Optional.of(vardenhet);
    }

    private Invanare buildInvanare() {
        Personnummer personnummer = createPersonnummer("191212121212").get();
        return new Invanare(1L, personnummer, "");
    }

    private Handlaggare buildHandlaggare() {
        return new Handlaggare(1L, "", "", "", "", "", "", "", "", "");
    }

    private Bestallning buildBestallning() {
        return Bestallning.Factory.newBestallning(buildInvanare(), "syfte", "insats", buildHandlaggare(), "intygsTyp", 1.0, buildVardenhet().get(), "ref");
    }

    private se.inera.intyg.infra.integration.hsa.model.Vardenhet buildVardenhetFromHsa() {
        se.inera.intyg.infra.integration.hsa.model.Vardenhet vardenhet =
                new se.inera.intyg.infra.integration.hsa.model.Vardenhet();
        vardenhet.setVardgivareHsaId(HSA_ID);
        vardenhet.setVardgivareOrgnr(ORGNUMMER);
        vardenhet.setId("vardenhetID");
        vardenhet.setNamn("vardenhetNamn");
        vardenhet.setArbetsplatskod("12345");
        vardenhet.setEpost("e@post.com");
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("1234556");
        return vardenhet;
    }

    private se.inera.intyg.infra.integration.hsa.model.Vardenhet buildVardenhetWithoutEmail() {
        se.inera.intyg.infra.integration.hsa.model.Vardenhet vardenhet =
                new se.inera.intyg.infra.integration.hsa.model.Vardenhet();
        vardenhet.setVardgivareHsaId(HSA_ID);
        vardenhet.setVardgivareOrgnr(ORGNUMMER);
        vardenhet.setId("vardenhetID");
        vardenhet.setNamn("vardenhetNamn");
        vardenhet.setArbetsplatskod("12345");
        vardenhet.setPostadress("postadress");
        vardenhet.setPostnummer("12345");
        vardenhet.setPostort("postort");
        vardenhet.setTelefonnummer("1234556");
        return vardenhet;
    }

}
