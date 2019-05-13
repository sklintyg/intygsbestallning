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

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import com.google.common.base.Strings;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentResponseType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.OrderAssessmentType;
import se.riv.intygsbestallning.certificate.order.orderassessment.v1.rivtabp21.OrderAssessmentResponderInterface;
import se.riv.intygsbestallning.certificate.order.v1.CVType;
import se.riv.intygsbestallning.certificate.order.v1.CitizenType;
import se.riv.intygsbestallning.certificate.order.v1.IIType;
import se.inera.intyg.infra.integration.pu.util.PersonIdUtil;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequest;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestHandlaggare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestInvanare;
import se.inera.intyg.intygsbestallning.common.dto.CreateBestallningRequestKontor;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationErrorCode;
import se.inera.intyg.intygsbestallning.common.exception.IbResponderValidationException;
import se.inera.intyg.intygsbestallning.common.property.IntegrationProperties;
import se.inera.intyg.intygsbestallning.common.service.bestallning.BestallningTextService;
import se.inera.intyg.intygsbestallning.common.util.RivtaUtil;
import se.inera.intyg.schemas.contract.Personnummer;

@Component
@Transactional(noRollbackFor = IbResponderValidationException.class)
public class OrderAssessmentIntygsbestallning implements OrderAssessmentResponderInterface {
    protected static final String ATTR_PURPOSE = "purpose";
    protected static final String ATTR_PLANNED_ACTIONS = "plannedActions";
    protected static final String ATTR_CASE_REFERENCE = "CaseReference";
    protected static final String ATTR_CITIZEN_SITUATION_BACKGROUND
            = "citizen.situationBackground";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_COST_CENTER
            = "authorityAdministrativeOfficial.officeCostCenter";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_NAME
            = "authorityAdministrativeOfficial.officeName";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_FULL_NAME
            = "authorityAdministrativeOfficial.fullName";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_PHONE_NUMBER
            = "authorityAdministrativeOfficial.phoneNumber";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_ADDRESS
            = "authorityAdministrativeOfficial.officeAddress.postalAddress";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CODE
            = "authorityAdministrativeOfficial.officeAddress.postalCode";
    protected static final String ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CITY
            = "authorityAdministrativeOfficial.officeAddress.postalCity";

    private static final String KV_FORMULAR = "fe11ea2d-9c5f-4786-b98f-bd5e6c93ea72";
    private static final String KV_MYNDIGHET = "769bb12b-bd9f-4203-a5cd-fd14f2eb3b80";
    private static final String HSA_ID_ROOT = "1.2.752.129.2.1.4.1";

    private static final int STRING_64_CHARS = 64;
    private static final int STRING_4000_CHARS = 4000;
    private static final int STRING_255_CHARS = 255;

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private CreateBestallningService createBestallningService;
    private IntegrationProperties integrationProperties;
    private BestallningTextService bestallningTextService;

    public OrderAssessmentIntygsbestallning(
            CreateBestallningService createBestallningService,
            IntegrationProperties integrationProperties,
            BestallningTextService bestallningTextService) {
        this.createBestallningService = createBestallningService;
        this.integrationProperties = integrationProperties;
        this.bestallningTextService = bestallningTextService;
    }

    @Override
    public OrderAssessmentResponseType orderAssessment(String logicalAddress, OrderAssessmentType orderAssessmentType) {

        LOG.info("Received request for OrderAssessment");

        var result = Try.of(() -> {
            var createBestallningRequest = fromType(orderAssessmentType);
            var id = createBestallningService.create(createBestallningRequest);
            var response = new OrderAssessmentResponseType();
            response.setAssessmentId(RivtaUtil.createIIType(integrationProperties.getSourceSystemHsaId(), id.toString()));
            response.setResult(RivtaUtil.createResultTypeOk());
            return response;
        });

        if (result.isSuccess()) {
            return result.get();
        } else {
            LOG.error("Error in orderAssessment", result.getCause());
            var response = new OrderAssessmentResponseType();
            response.setResult(RivtaUtil.createResultTypeError(result.getCause()));
            return response;
        }
    }

    private CreateBestallningRequest fromType(OrderAssessmentType request) {

        var personnummerString = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getExtension).get();

        var personnummerRoot = Optional.ofNullable(request.getCitizen())
                .map(CitizenType::getPersonalIdentity)
                .map(IIType::getRoot).get();

        var personnummer = Personnummer.createPersonnummer(personnummerString)
                .orElseThrow(() -> new IbResponderValidationException(
                        IbResponderValidationErrorCode.GTA_FEL05,
                        List.of(personnummerString)));

        // Since createPersonnummer is lax by design, also check that input matches NORMALIZED personnummer.
        if (!personnummerString.equals(personnummer.getPersonnummer())) {
            throw new IbResponderValidationException(
                    IbResponderValidationErrorCode.GTA_FEL05,
                    List.of(personnummerString));
        }

        if ((!PersonIdUtil.isSamordningsNummer(personnummer) && !personnummerRoot.equals(PersonIdUtil.getPersonnummerRoot()))
                || (PersonIdUtil.isSamordningsNummer(personnummer) && !personnummerRoot.equals(PersonIdUtil.getSamordningsNummerRoot()))) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(personnummerRoot));
        }

        final var vardenhet = Optional.of(request.getCareUnitId()).map(IIType::getExtension).get();
        var vardenhetRoot = Optional.of(request.getCareUnitId()).map(IIType::getRoot).get();
        if (!vardenhetRoot.equals(HSA_ID_ROOT)) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(vardenhetRoot));
        }

        var typ = Optional.ofNullable(request.getOrderFormType()).map(CVType::getCode).get();
        var typCodeSystem = Optional.ofNullable(request.getOrderFormType()).map(CVType::getCodeSystem).get();

        if (!typCodeSystem.equals(KV_FORMULAR)) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(typCodeSystem));
        }

        validateStringAttributeLengths(request);

        var texter = bestallningTextService.getBestallningTexter(typ);
        if (texter.isEmpty()) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL08, List.of());
        }

        final var invanare = new CreateBestallningRequestInvanare(personnummer, request.getCitizen().getSituationBackground());

        CreateBestallningRequestKontor kontor;
        if (request.getAuthorityAdministrativeOfficial().getOfficeAddress() != null) {
            kontor = new CreateBestallningRequestKontor(
                    request.getAuthorityAdministrativeOfficial().getOfficeName(),
                    request.getAuthorityAdministrativeOfficial().getOfficeCostCenter(),
                    request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalAddress(),
                    request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalCode(),
                    request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalCity());
        } else {
            kontor = new CreateBestallningRequestKontor(
                    request.getAuthorityAdministrativeOfficial().getOfficeName(), null, null, null, null);
        }

        var auktoritet = new CreateBestallningRequestHandlaggare(
                request.getAuthorityAdministrativeOfficial().getFullName(),
                request.getAuthorityAdministrativeOfficial().getPhoneNumber(),
                request.getAuthorityAdministrativeOfficial().getAuthority().getCode(),
                kontor);

        var auktoritetSystemKod = request.getAuthorityAdministrativeOfficial().getAuthority().getCodeSystem();
        if (!auktoritetSystemKod.equals(KV_MYNDIGHET)) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL01, List.of(auktoritetSystemKod));
        }

        var mappadMyndighet = Try.of(() -> MyndighetTyp.valueOf(auktoritet.getMyndighet()));
        if (mappadMyndighet.isFailure()) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL02, List.of(auktoritet.getMyndighet()));
        }

        return new CreateBestallningRequest(
                invanare,
                auktoritet,
                request.getPurpose(),
                request.getPlannedActions(),
                texter.get().getTyp(),
                texter.get().getIntygTyp(),
                texter.get().getIntygTypBeskrivning(),
                vardenhet,
                request.getCaseReference());
    }

    private void validateStringAttributeLengths(OrderAssessmentType request) {
        validateTextAttributeMaxLength(request.getPurpose(), ATTR_PURPOSE, STRING_4000_CHARS);
        validateTextAttributeMaxLength(request.getPlannedActions(), ATTR_PLANNED_ACTIONS, STRING_4000_CHARS);
        validateTextAttributeMaxLength(request.getCaseReference(), ATTR_CASE_REFERENCE, STRING_64_CHARS);
        validateTextAttributeMaxLength(request.getCitizen().getSituationBackground(), ATTR_CITIZEN_SITUATION_BACKGROUND, STRING_4000_CHARS);

        if (request.getAuthorityAdministrativeOfficial() != null) {
            validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getOfficeCostCenter(),
                    ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_COST_CENTER, STRING_64_CHARS);
            validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getOfficeName(),
                    ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_NAME, STRING_255_CHARS);
            validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getFullName(),
                    ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_FULL_NAME, STRING_255_CHARS);
            validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getPhoneNumber(),
                    ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_PHONE_NUMBER, STRING_64_CHARS);
            if (request.getAuthorityAdministrativeOfficial().getOfficeAddress() != null) {
                validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalAddress(),
                        ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_ADDRESS, STRING_255_CHARS);
                validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalCode(),
                        ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CODE, STRING_64_CHARS);
                validateTextAttributeMaxLength(request.getAuthorityAdministrativeOfficial().getOfficeAddress().getPostalCity(),
                        ATTR_AUTHORITY_ADMINISTRATIVE_OFFICIAL_OFFICE_ADDRESS_POSTAL_CITY, STRING_255_CHARS);
            }
        }
    }

    private void validateTextAttributeMaxLength(String attributeValue, String attributeName, int validMaxLength) {
        if (Strings.nullToEmpty(attributeValue).length() > validMaxLength) {
            throw new IbResponderValidationException(IbResponderValidationErrorCode.GTA_FEL13, List.of(attributeName));
        }

    }

    private enum MyndighetTyp {
        AF("Arbetsförmedlingen"),
        HSVARD("Hälso- och sjukvården"),
        INVANA("Invånaren"),
        TRANSP("Transportstyrelsen"),
        SOS("Socialstyrelsen"),
        SK("Skatteverket");

        private final String description;

        MyndighetTyp(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;

        }
    }
}
